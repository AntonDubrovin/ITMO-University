package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.CommentForm;
import ru.itmo.wp.form.PostForm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CommentForm.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            CommentForm commentForm = (CommentForm) target;

            if (StringUtils.isEmpty(commentForm.getText())) {
                errors.rejectValue("text", "text.invalid-text", "text should not be empty");
            }

            if (commentForm.getText().matches("[ ]*")) {
                errors.rejectValue("text", "text.invalid-text", "text can't contains only whitespaces");
            }
        }
    }
}
