package com.iqbal.cap.zcap_sqllite.handlers;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.stereotype.Component;

import com.sap.cds.ql.Select;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;

import cds.gen.bookservice.Authors;
import cds.gen.bookservice.Authors_;
import cds.gen.bookservice.BookService_;
import cds.gen.bookservice.Books;
import cds.gen.bookservice.Books_;

@Component
@ServiceName(BookService_.CDS_NAME)
public class BookServiceHandler implements EventHandler {

  private final PersistenceService db;

  public BookServiceHandler(PersistenceService db) {
    this.db = db;
  }

  @Before(event = CqnService.EVENT_CREATE, entity = Books_.CDS_NAME)
  public void beforeCreateBook(CdsCreateEventContext context, Books book) {
    // Get the book data from the context

    if (book.getTitle() == null || book.getTitle().isEmpty()) {
      throw new RuntimeException("Book title is required!");
    }
  }

  @Before(event = CqnService.EVENT_CREATE, entity = Books_.CDS_NAME)
  public boolean validateAuthor(CdsCreateEventContext context, Books book, Authors author) {
    String authorID = author.getId();
    if (author.getId() == null || author.getId().isEmpty()) {
      throw new RuntimeException("Book title is required!");
    }

    Result result = (Result) db.run(Select.from(Authors_.class).columns(Authors_.ID).where(a -> a.ID().eq(authorID)));
    boolean authorExists = ((com.sap.cds.Result) result).first().isPresent();
    System.out.println("Author exists: " + authorExists);
    return authorExists;
  }

}