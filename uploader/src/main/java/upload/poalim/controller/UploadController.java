package upload.poalim.controller;

import entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import upload.poalim.OCRUtil.OcrService;
import upload.poalim.OCRUtil.S3Services;



//main controller
@Controller
public class UploadController {

    @Autowired
    OcrService ocrService;

    @Value("${customer.url}")
    String customerUrl;

    @Value("${search.url}")
    String searchUrl;

    @Autowired
    S3Services s3Services;

    @Value("${gkz.s3.bucket}")
    private String bucketName;


    @GetMapping("/index")
    public String index() {
        return "upload";
    }


    @GetMapping("/customer")
    public String greetingForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "uploadCustomer";
    }


    @RequestMapping("/defect-details")
    public String defectDetails(Model model) {
        model.addAttribute("customer", new Customer());
        return "uploadCustomer"; //defect-details.html page name to open it
    }


    /// uploading customer to bank repository
    @PostMapping("/customer")
    public String uploadCustomer(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(
                    customerUrl, String.class, customer.getId(), customer.getName());

            if (response == null || !response.equals("OK")) {
                redirectAttributes.addFlashAttribute("message", "failed insert customer!");
                return "redirect:uploadStatus";
            } else {
                String msg = String.format(" insert customer success!  name:%1$s , account number:%2$s ", customer.getName(), customer.getId());
                redirectAttributes.addFlashAttribute("message", msg);
                return "redirect:uploadStatus";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "failed insert customer!");
        }
        return "uploadCustomer";
    }


    // uploading file and check name by id
    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            //upload file to s3 to use with text aws-rekognize
            s3Services.uploadFile(file.getOriginalFilename(), file);

            //parse the image to text
            String accountNumber = ocrService.parseOcr(file.getOriginalFilename(),bucketName).replaceAll("\n", "");

            //parsing failed
            if (accountNumber == null || accountNumber.contains("error")) {
                redirectAttributes.addFlashAttribute("message",
                        "cannot parse account number from image , please try again !" + accountNumber);
                return "redirect:/uploadStatus";
            }

            // get name from bank customer db
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(
                    searchUrl, String.class, accountNumber);

            if (response == null || response.equalsIgnoreCase("customerNotFound")) {
                redirectAttributes.addFlashAttribute("message",
                        "failed finding the customer name in database by image , account number on check is: " + accountNumber);
                return "redirect:/uploadStatus";

            }

            redirectAttributes.addFlashAttribute("message",
                    "The name for account " + accountNumber + " is: " + response + " ,thanks!");

        } catch (Exception ee) {
            System.out.println("error" + ee.getMessage());
            redirectAttributes.addFlashAttribute("message",
                    ee.getMessage() + "cannot upload : '" + file.getOriginalFilename() + "'" + ee.getMessage());

        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
