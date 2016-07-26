# CustomTabsSample
Chrome Custom Tabs sample for Android

##Set up

Use sparse-checkout submodule for gradle build.

```
$ git clone git@github.com:sakebook/CustomTabsSample.git
$ cd custom-tabs-client/
$ git config core.sparsecheckout true
$ echo shared/ > ../.git/modules/custom-tabs-client/info/sparse-checkout
$ cd ..
$ git read-tree -mu HEAD
```