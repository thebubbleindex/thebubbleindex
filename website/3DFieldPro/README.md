# 3DFieldPro

Contains a Windows VBScript (`recursive-json-dxf-files.vbs`) that recursively walks a directory tree and calls [3DFieldPro](https://3dfieldpro.com/) (`FieldPro64.exe`) on every `.csv` file it finds, generating DXF contour files from the Bubble Index XYZ data.

## Usage

1. Install [3DFieldPro](https://3dfieldpro.com/) on a Windows machine.
2. Open `recursive-json-dxf-files.vbs` in a text editor and update the `startDir` constant to point to your local Bubble Index output directory.
3. Run the script:
   ```
   cscript recursive-json-dxf-files.vbs
   ```

The script will produce a `values.dxf` contour file alongside each `.csv` file it processes.

## See Also

- [utilities/CreateXYZFiles](../../utilities/CreateXYZFiles/) — creates the (x, y, z) CSV files processed by this script
- [utilities/CreateHTML3DJson](../../utilities/CreateHTML3DJson/) — alternative browser-based 3D visualisation
