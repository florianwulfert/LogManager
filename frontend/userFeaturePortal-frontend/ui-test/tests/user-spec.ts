import {AfterSuite, BeforeSuite, Step} from "gauge-ts";
import {
  below,
  button,
  click,
  dropDown,
  goto,
  openBrowser,
  text,
  textBox,
  timeField,
  toRightOf,
  waitFor,
  write
} from 'taiko';

export default class UserSpec {
  @BeforeSuite()
  public async beforeSuite() {
    await openBrowser({headless: false});
  }

  @AfterSuite()
  public async afterSuite() {
    //await closeBrowser();
  };

  @Step("Open User Feature Portal")
  public async openApplication() {
    await goto("localhost:4200");
  }

  @Step("Login")
  public async login() {
    await write('Florian', {id: "inputName"});
    // await write('Eugen', textBox(below("Sign in")));
    await click('Apply');
  }

  @Step("Click User")
  public async clickUser() {
    await click('User');
  }

  @Step("Find User")
  public async findUser() {
    await text('Florian').exists()
  }

  @Step("Create User")
  public async createUser() {
    await click("Create user")
    await write('Hans', textBox("Name"))
    await timeField("Birthdate").select(new Date('2001-12-12'))
    await write('90', textBox("Weight"))
    await write('1.90', textBox("Height"))
    await click("Create")
    await click("Create user")
    await waitFor(1000)
  }

  @Step("Update User")
  public async updateUser() {
    await click("Update user")
    await waitFor(500)
    await dropDown(below("Update user")).select("Hans")
    /*await timeField("Birthdate").select(new Date('2001-08-12'))
    await write('91', textBox("Weight"))
    await write('1.88', textBox("Height"))
    await write("TestBuch", textBox("Favourite Book"))*/
    await click("Update")
  }

  @Step("Filter User")
  public async filterUser() {
    await write('Hans', textBox("Filter"))
    await text('Hans').exists()
  }

  @Step("Delete User")
  public async deleteUser() {
    await click(button(toRightOf("TestBuch")))
  }
}
