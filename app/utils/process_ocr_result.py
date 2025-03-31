from typing import List, Dict, Tuple


def calculate_center(vertices: List[Tuple]) -> Tuple:
    """

        This function calculates the center of a word. Each word has it's own box, and each box has 4 angles.
        The 4 angles are the 4 elements in the List, each element being tuple, that has two values, X and Y.

        The center of the word is later used to determine the order of the words, as well as noticing when there's
        new line or new paragraph in the text.

    :param vertices: List of four tuple elements, each tuple containing two elements, representing X and Y values
    for one point of the words' box.
    """
    top_left_x, top_left_y = vertices[0]
    top_right_x, top_right_y = vertices[1]
    bottom_right_x, bottom_right_y = vertices[2]
    bottom_left_x, bottom_left_y = vertices[3]

    x_center = (top_left_x + top_right_x + bottom_left_x + bottom_right_x) / 4
    y_center = (top_left_y + top_right_y + bottom_left_y + bottom_right_y) / 4

    return x_center, y_center


def sort_words(words: List[Dict]) -> List[Dict]:
    """

        The result from the OCR model are not in order i.e the model doesn't always read the text from top to bottom,
        and from left to right, therefore we need some processing to reformat the text.

        This functions sorts the words in the right order as the text in the image.

    """
    sorted_words = sorted(words, key=lambda item: item["text_object_center"][1])  # Sort top-to-bottom

    lines = []
    current_line = []
    y_threshold = 10  # Adjust this threshold based on the document's spacing

    for word in sorted_words:
        x_center, y_center = word["text_object_center"]

        if not current_line:
            current_line.append(word)
        else:
            prev_y_center = current_line[-1]["text_object_center"][1]

            if abs(y_center - prev_y_center) < y_threshold:
                current_line.append(word)
            else:
                lines.append(sorted(current_line, key=lambda w: w["text_object_center"][0]))  # Sort left-to-right
                current_line = [word]

    if current_line:
        lines.append(sorted(current_line, key=lambda w: w["text_object_center"][0]))  # Sort the last line

    return [word for line in lines for word in line]  # Flatten the list


def format_text(sorted_words: List[Dict]) -> str:
    """

        This function formats the text in a way that it detects where a new line should be added ('\n') or when a
        new paragraph appears and the text there should be intended ('\t')

        Input: List of dictionaries, each dictionary containing word, with vertices, confidence and center.

        Output: String, formatted text.

    """

    special_chars = {".", ",", "!", "?", ":", ";", ")", "]", "}"}
    opening_chars = {"(", "[", "{"}

    output_text = ""
    previous_word = None
    previous_y_center = None
    x_line_threshold = 30
    line_threshold = 15  # Adjust based on OCR spacing
    new_line_flag = False
    new_paragraph_flag = True

    for index, item in enumerate(sorted_words):
        word = item["word"].strip()

        if not word:
            continue  # Skip empty words

        x_center, y_center = item["text_object_center"]
        top_left_x_vertices = item["vertices"][0][0]

        # Detect new line (smaller vertical gap)
        if previous_y_center is not None and abs(y_center - previous_y_center) > line_threshold:
            output_text += "\n"
            new_line_flag = True
            new_paragraph_flag = True

        if top_left_x_vertices > x_line_threshold and new_paragraph_flag and previous_word is not None:
            output_text += "\t"

        new_paragraph_flag = False

        # Handle punctuation spacing
        if previous_word is None:  # First word
            output_text += word.capitalize()
        elif word in special_chars:
            output_text += word  # Attach punctuation directly
        elif previous_word in opening_chars:
            output_text += word  # No space after opening brackets
        else:
            if new_line_flag:
                output_text += word
                new_line_flag = False
            else:
                output_text += " " + word  # Normal spacing

        previous_word = word
        previous_y_center = y_center  # Update last Y-center

    return output_text


def correct_brackets_in_text(text):
    """

        This function corrects the brackets, checks if the open brackets match the closed brackets, if not fixes them,
        etc.

    """
    stack = []
    bracket_pairs = {')': '(', '}': '{', ']': '[', '>': '<', '\'': '\'', '\"': '\"'}
    opening_brackets = {'(': ')', '{': '}', '[': ']', '<': '>', '\'': '\'', '\"': '\"'}

    text_list = list(text)  # Convert text to a list for modification

    for i, char in enumerate(text_list):
        if char in opening_brackets:  # Opening bracket
            stack.append((char, i))  # Store the bracket and its index
        elif char in bracket_pairs:  # Closing bracket
            if stack and stack[-1][0] == bracket_pairs[char]:  # Correct match
                stack.pop()
            else:
                # Incorrect closing bracket, fix it
                if stack:
                    _, idx = stack.pop()
                    text_list[i] = opening_brackets[text_list[idx]]  # Correct closing bracket

    if len(stack) > 0:
        for _, idx in stack:
            text_list.pop(idx)

    return "".join(text_list)


def clean_unnecessary_characters(words: List[Dict]) -> List[Dict]:
    """

        This functions removes open brackets that are immediately closed and there's no text in between them, as well
        as open '"' characters and not text in them, etc.

        Example: This is a test ' " and some of the { ] chars here should be "removed".

        Example output: This is a test and some of the chars here should be "removed".

    """
    open_close_special_chars = ['\'', '\"', '{', '(', '[', '<', '}', ')', ']', '>']
    open_close_special_char_flag = False

    cleaned_list_of_words = []

    for item in words:
        word = item["word"]

        if word in open_close_special_chars and not open_close_special_char_flag:
            cleaned_list_of_words.append(item)
            open_close_special_char_flag = True
            continue

        if word in open_close_special_chars and open_close_special_char_flag:
            cleaned_list_of_words.pop()
            open_close_special_char_flag = False
            continue

        cleaned_list_of_words.append(item)
        open_close_special_char_flag = False

    return cleaned_list_of_words
