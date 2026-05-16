import { Stack } from "expo-router";
import { PaperProvider, MD3LightTheme, MD3DarkTheme } from "react-native-paper";
import { useColorScheme } from "react-native";

export default function RootLayout() {
  const scheme = useColorScheme();

  return (
    <PaperProvider theme={scheme === "dark" ? MD3DarkTheme : MD3LightTheme}>
      <Stack
        screenOptions={{
          headerStyle: {
            backgroundColor:
              scheme === "dark"
                ? MD3DarkTheme.colors.background
                : MD3LightTheme.colors.background,
          },
          headerTintColor:
            scheme === "dark"
              ? MD3DarkTheme.colors.onBackground
              : MD3LightTheme.colors.onBackground,
        }}
      />
    </PaperProvider>
  );
}
