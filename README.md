# Eclipse Complete Current Statement

<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=2686779" class="drag" title="Drag onto your running Eclipse toolbar to install Complete Current Statement for Eclipse"><img class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/_themes/solstice_marketplace/public/images/btn-install.png" alt="Drag onto your running Eclipse toolbar to install Complete Current Statement for Eclipse" /></a>

One of the main features Eclipse is missing is complete current statement shortcut, which adds 
either semicolon or curly brackets (braces?) at the end of the line, similar to the one in IntelliJ. This 
plugin tries to shamelessly mock it. As it's just regex comparison, it's nowhere near the level 
IJ is, but beats nothing.

Default shortcut is *Ctrl+Shift+Enter* in Java and JavaScript Editors, to reassign under keybindings (e.g. opened via double *Ctrl+Shift+L* ) search for _Complete line_

### To install, either 
- build on your own 
- use https://github.com/henri5/completeline-updatesite/raw/master/ as update site
- download the repo [completeline-updatesite](https://github.com/henri5/completeline-updatesite), extract it and use the folder as update site
- get it from marketplace https://marketplace.eclipse.org/content/complete-current-statement-eclipse
 
#### Features
* Open curly braces after class, method, interface, etc declaration
* Open curly braces after if (..), try, etc
* Open brackets after if, while, for, etc
* Add colon after case
* Add semicolon in the end of the line, if seems necessary
* Otherwise just insert and jump to next line

### License information:

![](https://github.com/henri5/completeline/raw/master/license.png)

<a href="http://with-eclipse.github.io/" target="_blank">
<img alt="with-Eclipse logo" src="http://with-eclipse.github.io/with-eclipse-0.jpg" />
</a>
