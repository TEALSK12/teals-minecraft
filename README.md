Teals Minecraft Project
====================================================================================================

This is the TEALS repo for the development of the Minecraft projects and associated curriculum.
The main project web site is at https://tealsk12.github.io/teals-minecraft.

**NOTE**: _All information below is for **developers** of the TEALS Minecraft project. To **use**
the Minecraft project (if you are an instructor or student), then [download the latest release] and
follow the instructions in top-level README file._


Prerequisites
-------------
This project is targeted for development on Windows. In addition to a standard Windows environment
(I'm running Win10), you should also have the following:

- Java 1.6+
- Either Eclipse or IntelliJ IDEA. Other IDE's may work; if so, please augment and document.


Clone Setup
-----------
In addition to the files and directories of a standard clone of this repo, you may also want to
set up a `test/` directory. This directory is ignored in the repo, and is intended for local test
setups of the TealsMC distributed zip file. You can use this directory to hold a built release
from the `build` directory. For example, running 'make build' might create
`out\tealsmc-idea-1.0.1.zip`, which you could then extract to `test` and try out from there.


Development - Eclipse
----------------------
1. At the root of the tree, run 'make dev-setup' to set up your local clone.

2. Start Eclipse. For the workspace choose the Forge directory at the root of your clone.

3. Run "File > New > Project...". Select "Java Project" and hit Next.

4. Project name = "production" (all lowercase)

5. Hit Finish.

6. Select Run > Run Configurations...

7. Select "Java Application". Hit the "New Launch Confuration" icon (it looks like a blank page
   with a plus sign, located in the upper left).

8. On "Main" tab, Main class = GradleStart. Hit the Run button.

9. After several seconds, you should see the Minecraft client splash screen.


Development - IntelliJ IDEA
---------------------------
1. At the root of the tree, run 'make dev-setup' to set up your local clone.

2. Start IntelliJ, and choose the "Open" option at the splash screen. Select the Forge folder at the
   root of your project clone.

3. Verify that you have the Java SDK set up. From the left pane, open
  `production/src/tealsmc.mods/blocks/BlocksModule`. If you see a message near the top of the file
  that the SDK hasn't yet been configured, hit the link and select the SDK.

4. Add a new configuration ("Run > Edit Configurations...").

5. Hit the plus symbol in the upper left. Choose "Application".

6. Name: `TealsMC Client`<br>
Main class: `GradleStart`<br>
Working directory: `...\Forge\production`

7. Hit OK to close the dialog box.

8. Run the TealsMC Client.


Building
--------
Use the `make.cmd` file at the root of the project to build the various components of the TealsMC
project. Generated files will go to the root `out` folder. Run `make.cmd` without arguments to see a
list of options. The more important targets are:

- build — Generates the `tealsmc-x.y.z-eclipse.zip` and `tealsmc-x.y.z-idea.zip` archives for
  distribution to students.


Releasing
---------
1. Ensure that `version.txt` contains the current release number. This project uses
   [semantic versioning](http://semver.org/). Semantic versioning uses three values:
   MAJOR.MINOR.PATCH.
   - Increment the PATCH number if you're making a bug fix that is backwards compatible with the prior
     release.
   - Increment the MINOR number (and reset PATCH to zero) if you're adding functionality that is
     backwards compatible with the prior release.
   - Increment the MAJOR number (and reset MINOR and PATCH values to zero) if a release is somehow
     backwards incompatible with the prior release.

2. Verify that `CHANGELOG.md` contains a good description of the changes for this release.

3. Update the release date in `CHANGELOG.md`.

4. Review `build/README-Distrib.txt` for content and make any necessary changes.

5. Build the new releases using `make all` from the root of the project. This will create the
   release Zip files in the `out/` directory.

6. Create a release tag at the current release point:

   ```
   > git tag -a vX.Y.Z
   ```

   This will create the tag `vX.Y.Z` at the current HEAD commit. In the editor, add all of the
   relevant changelog items from `CHANGELOG.md`. When done, push the new release tag up to GitHub:

   ```
   > git push origin vX.Y.Z
   ```

7. Now go to the
   [`teals-minecraft` release page](https://github.com/TEALSK12/teals-minecraft/releases) and hit
   the "Draft a new release" button. Enter the new release tag, ensure that the target branch is
   `master`, enter the title (for example, "v1.1.0"). Finally add the changelog items in the
   description. After this, drag the release archives (`tealsmc-X.Y.Z-eclipse.zip` and
   `tealsmc-X.Y.Z-intellij.zip`) to the upload area. Finally, hit the "Publish release" button.

8. Bump the version in the `version.txt` file in the project root.

9. Start a new release section in `CHANGELOG.md`.


----
See the [TEALS Minecraft wiki](https://github.com/TEALSK12/teals-minecraft/wiki) for more project
information.


[download the latest release]: https://github.com/TEALSK12/teals-minecraft/releases/
