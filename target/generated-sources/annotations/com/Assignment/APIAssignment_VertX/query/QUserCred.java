package com.Assignment.APIAssignment_VertX.query;

import io.ebean.typequery.PString;

/**
 * Query bean for UserCred.
 * <p>
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@SuppressWarnings("unused")
@io.ebean.typequery.Generated("io.ebean.querybean.generator")
@io.ebean.typequery.TypeQueryBean("v1")
public final class QUserCred extends io.ebean.typequery.QueryBean<com.Assignment.APIAssignment_VertX.UserCred,QUserCred> {

  private static final QUserCred _alias = new QUserCred(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QUserCred alias() {
    return _alias;
  }

  public PString<QUserCred> username;
  public PString<QUserCred> password;


  /**
   * Return a query bean used to build a FetchGroup.
   * <p>
   * FetchGroups are immutable and threadsafe and can be used by many
   * concurrent queries. We typically stored FetchGroup as a static final field.
   * <p>
   * Example creating and using a FetchGroup.
   * <pre>{@code
   * 
   * static final FetchGroup<Customer> fetchGroup = 
   *   QCustomer.forFetchGroup()
   *     .shippingAddress.fetch()
   *     .contacts.fetch()
   *     .buildFetchGroup();
   * 
   * List<Customer> customers = new QCustomer()
   *   .select(fetchGroup)
   *   .findList();
   * 
   * }</pre>
   */
  public static QUserCred forFetchGroup() {
    return new QUserCred(io.ebean.FetchGroup.queryFor(com.Assignment.APIAssignment_VertX.UserCred.class));
  }

  /** Construct using the default Database */
  public QUserCred() {
    super(com.Assignment.APIAssignment_VertX.UserCred.class);
  }

  /** @deprecated migrate to query.usingTransaction() */
  @Deprecated(forRemoval = true)
  public QUserCred(io.ebean.Transaction transaction) {
    super(com.Assignment.APIAssignment_VertX.UserCred.class, transaction);
  }

  /** Construct with a given Database */
  public QUserCred(io.ebean.Database database) {
    super(com.Assignment.APIAssignment_VertX.UserCred.class, database);
  }


  /** Private constructor for Alias */
  private QUserCred(boolean dummy) {
    super(dummy);
  }

  /** Private constructor for FetchGroup building */
  private QUserCred(io.ebean.Query<com.Assignment.APIAssignment_VertX.UserCred> fetchGroupQuery) {
    super(fetchGroupQuery);
  }

  /** Private constructor for filterMany */
  private QUserCred(io.ebean.ExpressionList<com.Assignment.APIAssignment_VertX.UserCred> filter) {
    super(filter);
  }

  /** Return a copy of the query bean. */
  @Override
  public QUserCred copy() {
    return new QUserCred(query().copy());
  }

  /**
   * Provides static properties to use in <em> select() and fetch() </em>
   * clauses of a query. Typically referenced via static imports. 
   */
  @io.ebean.typequery.Generated("io.ebean.querybean.generator")
  public static final class Alias {
    public static PString<QUserCred> username = _alias.username;
    public static PString<QUserCred> password = _alias.password;
  }

  /** Association query bean */
  @io.ebean.typequery.Generated("io.ebean.querybean.generator")
  @io.ebean.typequery.TypeQueryBean("v1")
  public static final class Assoc<R> extends io.ebean.typequery.TQAssocBean<com.Assignment.APIAssignment_VertX.UserCred,R,QUserCred> {
    public PString<R> username;
    public PString<R> password;

    public Assoc(String name, R root) {
      super(name, root);
    }

    public Assoc(String name, R root, String prefix) {
      super(name, root, prefix);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public R filterMany(java.util.function.Consumer<QUserCred> apply) {
      final io.ebean.ExpressionList list = io.ebean.Expr.factory().expressionList();
      final var qb = new QUserCred(list);
      apply.accept(qb);
      expr().filterMany(_name).addAll(list);
      return _root;
    }
  }
}
