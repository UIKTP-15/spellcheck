//loader
window.addEventListener("load", () => {
    const loader = document.querySelector(".loader");
    loader.classList.add("loader-hidden");
    loader.addEventListener("transitionend", () => {
        document.body.removeChild(loader);
    });
});

function buttonShow(event) {
    var parentDiv = event.target.closest('.more-horizontal-wrapper');
    var dotsImage = parentDiv.querySelector('.dots-img');
    dotsImage.style.display = 'none';
    var buttons = parentDiv.querySelectorAll('.button');
    buttons.forEach(function (button) {
        button.style.display = 'inline-block';
    });
}


function edit(event, itemId) {
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
            fetch(`/history/edit/${itemId}?newName=${newFileName}`)
                .then(r => renameDialog.style.display = 'none')
        }
        // task.querySelector('.text-wrapper-6').textContent = newFileName;
    };
}


function deleteFile(event, itemId) {
    var parentDiv = event.target.closest('.more-horizontal-wrapper');
    var task = parentDiv.closest('.task');
    var deleteDialog = document.getElementById('deleteDialog');


    deleteDialog.style.display = 'block';


    document.getElementById('confirmDelete').onclick = function () {
        fetch(`/history/delete/${itemId}`, {
            method: "DELETE"
        }).then(response => {
            console.log(response)
            deleteDialog.style.display = 'none';
        })
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