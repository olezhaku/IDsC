import { useEffect, useMemo, useState } from "react";
import { FlatList } from "react-native";
import { FAB } from "react-native-paper";
import { Stack, useRouter } from "expo-router";

import Layout from "./shared/layouts/Layout";
import TextInput from "./shared/UI/TextInput/TextInput";
import List from "./shared/widgets/List/List";
import { useDevices } from "./shared/hooks/useDevices";
import { IDevice } from "./shared/types";

const App = () => {
  const { devices } = useDevices();
  const [search, setSearch] = useState("");
  const [debounced, setDebounced] = useState(search);
  const router = useRouter();

  useEffect(() => {
    const t = setTimeout(() => setDebounced(search), 200);
    return () => clearTimeout(t);
  }, [search]);

  const indexed = useMemo(() => {
    return devices.map((d) => ({
      ...d,
      _search:
        `${d.manufacturer} ${d.marketing_name} ${d.chipset}`.toLowerCase(),
    }));
  }, [devices]);

  const filtered = useMemo(() => {
    const q = debounced.toLowerCase();

    return indexed.filter((d) => d._search.includes(q));
  }, [debounced, indexed]);

  const searchHandler = (text: string) => {
    setSearch(text);
  };

  const pressHandler = () => {
    setSearch("");
  };

  const routeHandler = (id: number) => {
    router.push({
      pathname: "/device/[id]",
      params: { id: id },
    });
  };

  const renderItem = ({
    item,
    index,
  }: {
    item: IDevice & { _search: string };
    index: number;
  }) => (
    <List
      device={item}
      pressHandler={routeHandler}
      showDivider={index !== filtered.length - 1}
    />
  );

  return (
    <Layout>
      <Stack.Screen options={{ headerShown: false }} />

      <TextInput
        style={{ marginTop: 20 }}
        value={search}
        changeHandler={searchHandler}
        icon={search.length > 0 ? "close" : "magnify"}
        pressHandler={pressHandler}
      />

      <FlatList
        data={filtered}
        renderItem={renderItem}
        keyExtractor={(item) => String(item.id)}
        keyboardShouldPersistTaps="handled"
        contentContainerStyle={{ paddingBottom: 96 }}
      />

      <FAB
        icon="dice-5"
        style={{ position: "absolute", margin: 30, right: 0, bottom: 0 }}
        onPress={() => {
          const randomDevice =
            devices[Math.floor(Math.random() * devices.length)];
          routeHandler(randomDevice.id);
        }}
      />
    </Layout>
  );
};

export default App;
