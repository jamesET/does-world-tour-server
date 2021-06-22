//jshint strict: false
module.exports = function(config) {
  config.set({

    basePath: '../../../',

    files: [
      'src/main/webapp/bower_components/angular/angular.js',
      'src/main/webapp/bower_components/angular-route/angular-route.js',
      'src/main/webapp/bower_components/angular-mocks/angular-mocks.js',
      'src/main/webapp/components/**/*.js',
      'src/main/webapp/view*/**/*.js',
      'src/test/javascript/unit/**/*.js'
    ],

    autoWatch: true,

    frameworks: ['jasmine'],

    browsers: ['Chrome'],

    plugins: [
      'karma-chrome-launcher',
      'karma-firefox-launcher',
      'karma-jasmine',
      'karma-junit-reporter'
    ],

    junitReporter: {
      outputFile: 'target/test_out/unit.xml',
      suite: 'src/test/javascript/unit'
    }

  });
};
