# MindfulNest

## Getting Started

Below are the steps to build the MindfulNest app with the default demo classroom.

1. **Download the repository and open the project in Android Studio.** Current version of Android Studio as of writing this document is **3.4.1**.
2. **Download the app assets and resources from Box.** Access to the MindfulNest directory on Box is restricted. Please contact someone on the project directly if you need access to the directory.
3. **Choose the classroom/DB seed you want to use and hardcoded hardware values (optional)** by changing the values of `DATABASE_SEED`, `HARDCODED_VALUES` in `Constants.java`.
4. **Build/Run the application in Android Studio.** If you changed the value of `DATABASE_SEED` or made changes to the JSON file, you must first uninstall the app on the device, then Build/Run the app for changes to take effect.

### Creating Your Own Classroom

The project currently does not have any built-in capabilities for editing a classroom. If you wish to create your own classroom of students, you must provide a JSON file containing student names and images. You can use the provided JSON assets as a guide.
