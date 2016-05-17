package com.monsanto.arch.awsutil.auth.policy

/** A statement describes a rule for allowing or denying access to a specific
  * AWS resource based on how the resource is being accessed, and who is
  * attempting to access the resource.  Statements can also optionally contain
  * a list of conditions that specify when a statement is to be honored.
  *
  * @param id         the optional identifier for the statement
  * @param principals indicates the user (IAM user, federated user, or
  *                   assumed-role user), AWS account, AWS service, or other
  *                   principal entity that it allowed or denied access to a
  *                   resource.  Note that you can use [[Principal.allPrincipals]]
  *                   as a handy value.
  * @param effect     whether the statement allows or denies access
  * @param actions    indicates the ways in which the principals are trying to
  *                   interact with the resources
  * @param resources  the AWS entities the principal is trying to access
  * @param conditions optional constraints that indicate when to allow or deny
  *                   a principal access to a resources
  */
case class Statement(id: Option[String],
                     principals: Seq[Principal],
                     effect: Statement.Effect,
                     actions: Seq[Action],
                     resources: Seq[Resource],
                     conditions: Seq[Condition]) {
  principals match {
    case Seq(Principal.AllPrincipals) ⇒
      // this is OK, AllPrincipals by itself is acceptable
    case ps if ps.contains(Principal.AllPrincipals) ⇒
      // this is OK, AllPrincipals by itself is acceptable
      throw new IllegalArgumentException("You may only use the AllPrincipals by itself.")
    case _ ⇒
      // all other cases should be ok
  }
}

object Statement {
  /** Enumeration type for statement effects. */
  sealed trait Effect
  object Effect {
    /** Explicitly allow access. */
    case object Allow extends Effect
    /** Explicitly deny access. */
    case object Deny extends Effect

    val values: Seq[Effect] = Seq(Allow,Deny)
  }
}
