import {Step, Table, BeforeSuite, AfterSuite} from "gauge-ts";
import {strictEqual} from 'assert';
import {
  below,
  checkBox,
  click,
  closeBrowser,
  evaluate,
  goto,
  intercept,
  into,
  link,
  openBrowser,
  press,
  text,
  textBox,
  toLeftOf,
  write
} from 'taiko';
import assert = require("assert");
import {USERS} from "../mock-data/mock-data";

export default class UserSpec {
  @BeforeSuite()
  public async beforeSuite() {
    await openBrowser({headless: true});
  }

  @AfterSuite()
  public async afterSuite() {
    await closeBrowser();
  };

  @Step("Open User Feature Portal")
  public async openApplication() {
    await goto("localhost:4200");
  }

  @Step("Login")
  public async login() {
    await intercept("http://localhost:8081/users", USERS)
    await write('Alf', {id: "inputName"});
    // await write('Eugen', textBox(below("Sign in")));
    await click('Apply');
  }

  @Step("Click User")
  public async clickUser() {
    await click('User');
  }

  @Step("Find User")
  public async findUser() {
    await text('Eugen').exists()
  }
}
