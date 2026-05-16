import { NativeModules } from "react-native";
import { File, Paths } from "expo-file-system";

type ShellModule = {
  executeCommand: (command: string, callback: (result: string) => void) => void;
};

export class NoRootError extends Error {
  constructor() {
    super("Root not available");
    this.name = "NoRootError";
  }
}

const getShell = (): ShellModule | null => {
  const mod = (NativeModules as { AndroidShell?: ShellModule }).AndroidShell;
  if (!mod || typeof mod.executeCommand !== "function") return null;
  return mod;
};

const exec = (command: string) =>
  new Promise<string>((resolve, reject) => {
    const shell = getShell();
    if (!shell) {
      reject(new NoRootError());
      return;
    }
    let settled = false;
    const timer = setTimeout(() => {
      if (!settled) {
        settled = true;
        reject(new Error(`Command timed out: ${command}`));
      }
    }, 15000);
    try {
      shell.executeCommand(command, (result: string) => {
        if (settled) return;
        settled = true;
        clearTimeout(timer);
        resolve(result ?? "");
      });
    } catch (e) {
      if (settled) return;
      settled = true;
      clearTimeout(timer);
      reject(e);
    }
  });

export const useShell = () => {
  const executeScript = async (script: string) => {
    if (!getShell()) throw new NoRootError();

    const file = new File(Paths.cache, `run-${Date.now()}.sh`);
    if (file.exists) file.delete();
    file.create();
    file.write(script);

    const path = file.uri.replace(/^file:\/\//, "");

    try {
      await exec(`chmod 755 ${path}`);

      const suProbe = await exec(`which su`);
      if (!suProbe.trim()) throw new NoRootError();

      return await exec(`su -c ${path}`);
    } finally {
      try {
        file.delete();
      } catch {}
    }
  };

  return { executeScript };
};
