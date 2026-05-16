import { useState } from "react";
import { Stack, useLocalSearchParams } from "expo-router";
import { Divider, FAB, Text } from "react-native-paper";
import { ToastAndroid, View } from "react-native";

import Layout from "../shared/layouts/Layout";
import { useDevices } from "../shared/hooks/useDevices";
import { useShell, NoRootError } from "../shared/hooks/useShell";
import { createScript } from "../shared/utils/createScript";
import { IDevice } from "../shared/types";

const Device = () => {
  const { id } = useLocalSearchParams();
  const { getById, devices } = useDevices();
  const device = getById(Number(id));

  const fields = device
    ? Object.entries(device).filter(([key]) => key !== "id")
    : [];

  const [isWriting, setIsWriting] = useState(false);
  const [status, setStatus] = useState<string | null>(null);
  const { executeScript } = useShell();

  const run = async (device: IDevice) => {
    setIsWriting(true);
    setStatus("Executing script...");

    try {
      const script = createScript(device);
      const result = await executeScript(script);
      setStatus(`Done: ${result}`);
    } catch (e: any) {
      if (e instanceof NoRootError) {
        setStatus("Root not found");
      } else {
        setStatus(`Error: ${e.message}`);
      }
    } finally {
      setIsWriting(false);
    }
  };

  return (
    <Layout style={{ paddingTop: 0 }}>
      <Stack.Screen
        options={{
          title: `Device: ${id}/${devices.length}`,
          headerShadowVisible: false,
        }}
      />
      {device ? (
        <>
          {fields.map(([key, value], index) => (
            <View key={key}>
              <Text variant="titleMedium" style={{ padding: 3 }}>
                {key}: {String(value)}
              </Text>

              {index !== fields.length - 1 && <Divider />}
            </View>
          ))}

          <FAB
            icon="restart"
            style={{ position: "absolute", margin: 30, right: 0, bottom: 0 }}
            onPress={() => run(device)}
            loading={isWriting}
            disabled={isWriting}
          />

          {status && ToastAndroid.show(status, ToastAndroid.SHORT)}
        </>
      ) : (
        <Text variant="headlineMedium" style={{ textAlign: "center" }}>
          Device not found(
        </Text>
      )}
    </Layout>
  );
};

export default Device;
