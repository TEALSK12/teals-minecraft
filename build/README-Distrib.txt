TEALS Minecraft Project
================================================================================

The TEALS Minecraft project is a set of lessons based on the Minecraft Forge
<http://www.minecraftforge.net/> modding package. We have customized this
package to reduce the amount of overhead of modifying Minecraft, so that
students can easily create new kinds of items, blocks, and entities in a custom
Minecraft client.

Currently, our package supports the IntelliJ and Eclipse Java IDEs. Start off by
unzipping the archive (Eclipse or IDEA flavor) into a working directory on your
machine.


Eclipse
-------
For the Eclipse package, open the root of the project as a new workspace (it
should be named 'TealsMC-Eclipse'). You should be ready to go. Just hit the
run button (or Ctrl-F11 or F11) and the Minecraft client splash screen
should come up after a few seconds. Follow the instructions from Lab 1 to
start modding.


IntelliJ IDEA
-------------
Launch IntelliJ. At the IntelliJ IDEA splash screen, hit "Open", and select
the "TealsMC-IntelliJ/Forge" directory you extracted from the project zip
file.

You may see a warning about an improperly set up JDK for your project. This
can happen immediately when you open the project, or it might happen when you
try to run the client. If so, (and assuming you have installed a JDK,)
IntelliJ should automatically walk you through the steps needed to fix this.
If it's not obvious, follow these steps:

1. Launch 'File > Project Structure'.
2. Navigate to 'Project Settings / Project'.
3. We're expecting that the Project SDK chooser shows "Invalid" or something.
4. Hit the 'New...' button and select 'JDK'.
5. Now navigate to your JDK directory. For example, this might be in
   "C:\Program Files\Java\jdk1.8.0_131". (This would be a 64-bit version. A
   32-bit version would probably be under "C:\Program Files (x86)\Java\...".)
6. Once you've selected it, hit OK, and you should be good to go.

After that, run 'client', and you should see the Minecraft client splash screen
after several seconds.


Lessons and Labs
----------------
You can find the TEALS Minecraft lessons and labs on the project web site at
https://tealsk12.github.io/teals-minecraft/.


Help & Assistance
-----------------
We have a project wiki at <https://github.com/TEALSK12/teals-minecraft/wiki>
everyone can use and edit to share out information. Please add whatever
information you feel may help your fellow students and instructors!
