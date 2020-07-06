- **Get the installation zip archive**: Go to the releases location (https://github.com/owlcms/owlcms4/releases/latest) and get the current `zip` file from the `assets` section at the bottom of the release entry.

- Double-click on the zip file, and extract the files to a directory.  We suggest you use `~/owlcms4` as the unzipped location.

- Make sure you have Java 8 installed. 

  -  For Linux, refer to https://adoptopenjdk.net/releases.html depending on the Linux type you run
  -  For MacOS, see https://adoptopenjdk.net/releases.html#x64_mac

- To start the program, open a Terminal window,  directory to the location where you unzipped the files and launch Java as follows.  Assuming you extracted to a directory called `owlcms4` in your home, the following would work

  ```bash
  cd ~/owlcms4
  java -jar owlcms.jar
  ```
  This will actually start the program and a browser. See [Initial Startup](#initial-startup) for how to proceed.

  If you just want to use dummy data to practice (which will not touch the actual database), use instead:

  ```
  java -DdemoMode=true -jar owlcms.jar
  ```


## Initial Startup

When OWLCMS4 is started on a laptop, two windows are visible:  a command-line window, and an internet browser

- The command-line window (typically with a black background) is where the OWLCMS4 primary web server shows its execution log.  

  All the other displays and screens connect to the primary server.  <u>You can stop the program by clicking on the x</u> or clicking in the window and typing `Control-C`.  The various screens and displays will spin in wait mode until you restart the primary program -- there is normally no need to restart or refresh them.

- The white window is a normal browser.  If you look at the top, you will see two or more lines that tell you how to open more browsers and connect them to the primary server.

  ![060_urls](img\LocalInstall\060_urls.png)

  In this example the other laptops on the network would use the address `http://192.168.4.1:8080/` to communicate with the primary server.  "(wired)" refers to the fact that the primary laptop is connected via an Ethernet wire to its router -- see [Local Access](EquipmentSetup#local-access-over-a-local-network) for discussion.  When available, a wired connection is preferred.

  The address <u>depends on your own specific networking setup</u> and you must use one of the addresses displayed **on your setup.**  If none of the addresses listed work, you will need to refer to the persons that set up the networking at your site and on your laptop.  A "proxy" or a "firewall", or some other technical configuration may be blocking access, or requiring a different address that the server can't discover.

## Accessing the Program Files and Configuration

In order to uninstall owlcms4, to report problems, or to change some program configurations, you may need to access the program directory where you unzipped the files (the same where you start java from).

If you do so, you will see the installation directory content:

- `owlcms.exe` starts the owlcms server.  `demo-owlcms.exe` does the same, but using fictitious data that is reset anew on every start; this makes it perfect for practicing.

- `unins000.exe` is the unistaller.  It will cleanly uninstall everything (including the database and logs, so be careful)

- `database` contains a file ending in `.db` which contains competition data and is managed using the [H2 database engine](https://www.h2database.com/html/main.html). 

- `logs` contains the execution journal of the program where the full details of what happened are written. If you report bugs, you will be asked to send a copy of the files found in that directory (and possibly a copy of the files in the database folder as well).

- `local` is a directory that is used for translating the screens and documents to other languages, or to add alternate formats for results documents.

- `jre`  contains the Java Runtime Environment

- the file ending in `.jar` is the OWLCMS4 application in executable format

- the `owlcms.l4j.ini` file is used to override application settings (for example, to force the display language) or technical settings

## Control Access to the Application

Mischievous users can probably figure out your Wi-Fi network password, and gain access to the application. To prevent this, you will need to start the application with an extra parameter.

- `PIN` is an arbitrary strings of characters that will be requested when starting the first screen whenever you start a new session (typically, once per browser, or when the system is restarted). 

  ![B_PIN](img/Heroku/B_PIN.png)

- On Mac OS or Linux, you can give the PIN when starting the program, as follows

  ```bash
  java -DPIN=5612 -jar owlcms.jar
  ```

  or, alternately, as an environment variable that you can define using the `set` command or even dynamically when launching OWLCMS4. 

  ```bash
  PIN=5612 java -jar owlcms.jar
  ```

## Defining the language

You can use the same technique as for the PIN to force a language to be used on all the screens.  By default, OWLCMS4 will respect the browser settings.  To force a locale (say Canadian French, whose code is `fr_CA`)-- a locale is a language with possible per-country variations --  you can

-  define the Java system property `locale` (small letters) using the syntax 
  `java -Dlocale=fr_CA`
- Alternately, define the environment variable `LOCALE` with the value `fr_CA` 

If neither `-Dlocale` or `LOCALE` are defined, the [language setting](Preparation#display-language) from the competition information page is used.

## Configuration Parameters

See the [Configuration Parameters](Configuration.md  ' :include') page to see additional configuration options in addition to the ones presented on this page.