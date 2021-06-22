//jshint strict: false
exports.config = {

  allScriptsTimeout: 11000,

  specs: [
    'src/test/javascript/e2e-tests/*.js'
  ],

  capabilities: {
    'browserName': 'chrome'
  },

  baseUrl: 'http://localhost:8080/',

  framework: 'jasmine',

  jasmineNodeOpts: {
    defaultTimeoutInterval: 30000
  }

};
