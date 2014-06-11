/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 *
 * @author Aale
 */
public class PhantomJSUsage {

    public static void main(String[] args) {
        String phantomJSDir = "phantomjs/phantomjs.exe";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJSDir);
        PhantomJSDriver driver = new PhantomJSDriver(caps);

        String js = "var page = require('webpage').create(),\n"
                + "    system = require('system'),\n"
                + "    address,"
                + " result='';\n"
                + "\n"
                + "    address = arguments[0];\n"
                + "\n"
                + "    page.onResourceRequested = function (req) {\n"
                + "        result +=  JSON.stringify(req, undefined, 4);\n"
                + "    };\n"
                + "\n"
                + "    page.onResourceReceived = function (res) {\n"
                + "        result += JSON.stringify(res, undefined, 4);\n"
                + "    };\n"
                + "\n"
                + "    page.open(address, function (status) {\n"
                + "        if (status !== 'success') {\n"
                + "            console.log('FAIL to load the address');\n"
                + "        }\n"
                + ""
                + "        phantom.exit();\n"
                + "         return result;"
                + "    });"
                + "return result;\n"
                + "";
        String result = (String) driver.executePhantomJS(js, "https://github.com/ariya/phantomjs/blob/master/examples/netlog.js");
        System.out.println(result);

    }
}
