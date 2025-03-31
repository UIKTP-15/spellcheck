from google.cloud import vision
from typing import List, Tuple


class GoogleVision:

    @staticmethod
    def detect_document_text(path_or_content, is_local_file=True) -> List[Tuple]:
        """

            Input variables:
                path_or_content: path to image
                is_local_file: whether or not the image is local file

            Output: List of tuples, each tuple containing (word, box vertices, confidence)

            This function sends request to Google Vision API, which predicts the words in a handwritten text.
            The API returns a bunch of data, but we process it and keep only words' vertices, confidence and the word
            itself.

        """
        client = vision.ImageAnnotatorClient()

        if is_local_file:
            with open(path_or_content, 'rb') as image_file:
                content = image_file.read()
        else:
            content = path_or_content

        image = vision.Image(content=content)
        response = client.document_text_detection(image=image)
        document = response.full_text_annotation

        words = []
        if document:
            for page in document.pages:
                for block in page.blocks:
                    for paragraph in block.paragraphs:
                        for word in paragraph.words:
                            word_text = ''.join([symbol.text for symbol in word.symbols])
                            vertices = [(vertex.x, vertex.y) for vertex in word.bounding_box.vertices]
                            confidence = word.confidence
                            words.append((word_text, vertices, confidence))
        return words
