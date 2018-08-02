module.exports = (function () {
  return {
    local: { // localhost
      host: 'localhost',
      port: '3306',
      user: 'root',
      password: 'rlaehgus!973',
      database: 'likealarm'
    },
    real: { // real server db info
      host: '',
      port: '',
      user: '',
      password: '!',
      database: ''
    },
    dev: { // dev server db info
      host: '',
      port: '',
      user: '',
      password: '',
      database: ''
    }
  }
})();
