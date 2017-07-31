package de.htw.lenz.main;

import de.htw.lenz.gameUtils.Client;

public class MainSingleK {

  public static void main(String[] args) {
    if (args.length < 1) throw new Error("no host provided");
    new Client("Luis", args[0]);
  }

}
