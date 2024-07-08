package hiber;

import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MainApp {
   public static void main(String[] args) {
      var context = new AnnotationConfigApplicationContext(AppConfig.class);

      UserService userService = context.getBean(UserService.class);
      userService.cleanAllTables();

      userService.add(User.builder()
              .firstName("User1")
              .lastName("LastName1")
              .email("user1@mail.ru")
              .car(Car
                      .builder()
                      .model("Model1")
                      .series(5)
                      .build())
              .build());
      userService.add(User.builder()
              .firstName("User2")
              .lastName("LastName2")
              .email("user2@mail.ru")
              .car(Car
                      .builder()
                      .model("Model2")
                      .series(4)
                      .build())
              .build());
      userService.add(User.builder()
              .firstName("User3")
              .lastName("LastName3")
              .email("user3@mail.ru")
              .car(Car
                      .builder()
                      .model("Model3")
                      .series(3)
                      .build())
              .build());
      userService.add(User.builder()
              .firstName("User4")
              .lastName("LastName4")
              .email("user4@mail.ru")
              .car(Car
                      .builder()
                      .model("Model4")
                      .series(2)
                      .build())
              .build());

      List<User> users = userService.listUsers();
      for (User user : users) {
         System.out.println("Id = " + user.getId());
         System.out.println("First Name = " + user.getFirstName());
         System.out.println("Last Name = " + user.getLastName());
         System.out.println("Email = " + user.getEmail());
         System.out.println();
      }

      System.out.print("\n\n\nUSER BY CAR:\n\n");
      User userByCar = userService.findByCar("Model3", 3);
      System.out.println("Id = " + userByCar.getId());
      System.out.println("First Name = " + userByCar.getFirstName());
      System.out.println("Last Name = " + userByCar.getLastName());
      System.out.println("Email = " + userByCar.getEmail());

      context.close();
   }
}
