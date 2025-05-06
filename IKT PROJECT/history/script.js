
function buttonShow(event) {
    var parentDiv = event.target.closest('.more-horizontal-wrapper');
    var dotsImage = parentDiv.querySelector('.dots-img');
    dotsImage.style.display = 'none';

    var buttons = parentDiv.querySelectorAll('.button');
    buttons.forEach(function(button) {
        button.style.display = 'inline-block';
    });
}


function edit(event) {
    var parentDiv = event.target.closest('.more-horizontal-wrapper');
    var task = parentDiv.closest('.task');
    var fileName = task.querySelector('.text-wrapper-6').textContent;

    var renameDialog = document.getElementById('renameDialog');
    var newFileNameInput = document.getElementById('newFileName');
    newFileNameInput.value = fileName;

    renameDialog.style.display = 'block';

    document.getElementById('confirmRename').onclick = function () {
        var newFileName = newFileNameInput.value;
        if (newFileName) {
            task.querySelector('.text-wrapper-6').textContent = newFileName;
            renameDialog.style.display = 'none';
        }
    };
}


function deleteFile(event) {
    var parentDiv = event.target.closest('.more-horizontal-wrapper');
    var task = parentDiv.closest('.task');
    var deleteDialog = document.getElementById('deleteDialog');

    deleteDialog.style.display = 'block';

    document.getElementById('confirmDelete').onclick = function () {
        task.remove();
        deleteDialog.style.display = 'none';
    };
}


document.getElementById('closeDialog').onclick = function () {
    document.getElementById('renameDialog').style.display = 'none';
};

document.getElementById('closeDeleteDialog').onclick = function () {
    document.getElementById('deleteDialog').style.display = 'none';
};

document.getElementById('cancelDelete').onclick = function () {
    document.getElementById('deleteDialog').style.display = 'none';
};
