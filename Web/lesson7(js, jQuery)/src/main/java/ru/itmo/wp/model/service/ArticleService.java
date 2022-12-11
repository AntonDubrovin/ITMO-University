package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void validateCreationArticle(Article article) throws ValidationException {
        if (Strings.isNullOrEmpty(article.getTitle())) {
            throw new ValidationException("Please enter the title");
        }

        if (!article.getTitle().matches("[a-z]+")) {
            throw new ValidationException("Title can contain only lowercase Latin letters");
        }

        if (article.getTitle().length() <= 3) {
            throw new ValidationException("Title can not be shorter than 3 letters");
        }

        if (article.getTitle().length() >= 50) {
            throw new ValidationException("Title ca not be longer than 50 letters");
        }

        if (Strings.isNullOrEmpty(article.getText())) {
            throw new ValidationException("Please enter the text");
        }
    }

    public void createArticle(Article article) {
        articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
