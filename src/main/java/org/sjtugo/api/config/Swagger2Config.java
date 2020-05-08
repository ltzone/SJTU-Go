package org.sjtugo.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean("UsersApis")
    public Docket usersApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                // select()返回的是ApiSelectorBuilder对象，而非Docket对象
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                // build()返回的是Docket对象
                .build()
                // 测试API时的主机URL
                .host("api.ltzhou.com")
                // API前缀
                .pathProvider(new RelativePathProvider(null) {
                    @Override
                    public String getApplicationBasePath() {
                        return "/";
                    }
                })
                .apiInfo(apiInfo());
    }

    public ApiInfo apiInfo() {
        // API负责人的联系信息
        final Contact contact = new Contact(
                "Litao Zhou", "https://ltzhou.com", "ltzhou@sjtu.edu.cn");
        return new ApiInfoBuilder()
                // API文档标题
                .title("SJTU-Go系统平台接口文档")
                // API文档描述
                .description("SJTU-Go公共服务API说明, 关注微信小程序SJTU-Go")
                // 服务条款URL
                .termsOfServiceUrl("https://github.com/ltzone/SJTU-Go")
                // API文档版本
                .version("1.0")
                // API负责人的联系信息
                .contact(contact)
                // API的许可证Url
                .licenseUrl("http://www.apache.org/licenses/")
                .license("Apache License 2.0")
                .build();
    }
}