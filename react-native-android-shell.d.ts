declare module "@flyskywhy/react-native-android-shell" {
  const AndroidShell: {
    executeCommand(command: string, callback?: (result: string) => void): void;
  };

  export default AndroidShell;
}
