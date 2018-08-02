var multer = require('multer');
var fileType = require('file-type');

var upload = multer({
  dest: 'images/',
  limits: {fileSize: 100000000, files: 1},
  fileFilter: (req, headers, callback) => {
    if(!file.originalname.match(/\.(jpg|jpeg)$/)) {
      return callback(new Error('Only Images are allowed !'), false);
    }
    callback(null, true)
  }
}).single('image')
