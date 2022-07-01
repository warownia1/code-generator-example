package example;

import io.warownia1.processor.AutoMain;


class Example {

  public Example(String[] args) {
  }

  @AutoMain
  public void runMe() {
    System.out.println("Hello world");
  }

}