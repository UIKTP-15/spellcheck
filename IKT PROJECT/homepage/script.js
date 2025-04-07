

document.addEventListener('DOMContentLoaded', function() {
  const fileUpload = document.getElementById('file-upload');
  const uploadStatus = document.querySelector('.upload-status');
  const fileName = document.querySelector('.file-name');
  const cancelUpload = document.querySelector('.cancel-upload');
  const uploadButton = document.querySelector('.upload-button');
  const progressBarFill = document.querySelector('.progress-bar-fill');

  uploadButton.addEventListener('click', function() {
      fileUpload.click();
  });

  fileUpload.addEventListener('change', function(e) {
      const file = e.target.files[0];
      if (file) {
          uploadStatus.style.display = 'block';
          fileName.textContent = file.name;
          simulateUpload();
      }
  });

  cancelUpload.addEventListener('click', function() {
      uploadStatus.style.display = 'none';
      fileUpload.value = '';
      progressBarFill.style.width = '0%';
  });

  function simulateUpload() {
      let progress = 0;
      progressBarFill.style.width = '0%';
      progressBarFill.style.backgroundColor = 'rgba(8, 133, 134, 1)'; 

      const interval = setInterval(function() {
          if (progress >= 100) {
              clearInterval(interval);
          } else {
              progress += 10; 
              progressBarFill.style.width = progress + '%';
          }
      }, 200); 
  }

});
