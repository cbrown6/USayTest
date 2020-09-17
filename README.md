USayComTst - Selenium Application 
---------------------------------

I have coded as much as I could in 2 hrs.

It's not as I would have wished, where some corners have been cut, some requirements left out and standards overlooked...
to try to save time. Have included ChromeDriver in repository.


In order to run the Maven project, Edit "run / debug configurations" to:

"Add New JUnit Configuration" and configure:

Class: config.RunCukesTest

VM Options: -Dcountry=uk -Denv= -Dngsp=true -Dcucumber.options="--tags @homepageRegression"

Use classpath of module: ExtractImdbFilmInfo (...left over from previous project!)

