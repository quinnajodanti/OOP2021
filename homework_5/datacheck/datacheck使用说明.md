## Datacheck程序使用说明

#### 程序功能

本datacheck程序基于ALS调度策略度量给定输入的电梯运行时间。

#### 使用说明

1. 请将包含输入的文件以`stdin.txt`命名，放置在相同路径下。

2. 在命令行中执行二进制文件。

   例：

   对于Linux 64位操作系统：
   
   ```bash
   chmod u+x datacheck_student_linux_x86_64
./datacheck_student_linux_x86_64
   ```
   
   对于Windows操作系统：
   
   ```powershell
   .\datacheck_student_win64.exe
   ```
   
   对于macOS操作系统：
   
   ```sh
   chmod u+x datacheck_student_darwin
   ./datacheck_student_darwin
   ```
   
   仅提供对以上三种三种操作系统的支持。

对于合法输入，程序将输出$T_{base}$和$T_{max}$；对于非法输入，程序将尽可能定位错误原因。

提示：请根据操作系统使用对应的二进制文件，请在命令提示符(windows等)/终端(Linux, macOS等)中执行。