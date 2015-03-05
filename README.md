# AndroidResources
## Arrange Android resources

To use AndroidResources.jar you need Java and JDK 6 and later.
* Option A: Double click.* 
* Option B: From command line: ```java -jar <path to>/AndroidResources.jar```
* Drag and drop a floder with PNGs, if it contains filename formatted **"\*\_\<quality\>.png"**, 
it will create a folder for each **\<quality\>** type, move the image and remove **\_\<qaulity\>** from its name.

**For example, if you have these files in the folder**:
* menu_md.png
* menu_hd.png
* menu_xhd.png
* menu_xxhd

**Will be arranged**:
* md/menu.png
* hd/menu.png
* xhd/menu.png
* xxhd/menu.png


![Screenshot](https://github.com/rondesta/and_res/blob/master/screenshot.jpeg)
