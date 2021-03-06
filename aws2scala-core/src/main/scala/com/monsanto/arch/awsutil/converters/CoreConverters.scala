package com.monsanto.arch.awsutil.converters

import java.util

import com.amazonaws.auth.policy
import com.amazonaws.auth.policy.conditions._
import com.amazonaws.regions
import com.monsanto.arch.awsutil.auth.policy._
import com.monsanto.arch.awsutil.regions.Region

import scala.collection.JavaConverters._

/** Provides converters between core ''aws2scala'' objects and their AWS Java SDK counterparts. */
object CoreConverters {
  implicit class AwsPrincipal(val principal: policy.Principal) extends AnyVal {
    def asScala: Principal = Principal.fromProviderAndId(principal.getProvider, principal.getId)
  }

  implicit class ScalaPrincipal(val principal: Principal) extends AnyVal {
    def asAws: policy.Principal = {
      principal match {
        case Principal.AllPrincipals ⇒
          policy.Principal.All
        case Principal.AllUsers ⇒
          policy.Principal.AllUsers
        case Principal.ServicePrincipal(Principal.Service.AllServices) ⇒
          policy.Principal.AllServices
        case Principal.WebProviderPrincipal(Principal.WebIdentityProvider.AllProviders) ⇒
          policy.Principal.AllWebProviders
        case _ ⇒
          new policy.Principal(principal.provider, principal.id, false)
      }
    }
  }

  implicit class AwsAction(val action: policy.Action) extends AnyVal {
    def asScala: Action = {
      Action.toScalaConversions.get(action)
        .orElse(Action.fromName.unapply(action.getActionName))
        .getOrElse(Action.NamedAction(action.getActionName))
    }
  }

  implicit class ScalaAction(val action: Action) extends AnyVal {
    def asAws: policy.Action = Action.toAwsConversions(action)
  }

  implicit class AwsCondition(val condition: policy.Condition) extends AnyVal {
    def asScala: Condition =
      Condition.fromParts(condition.getConditionKey, condition.getType, condition.getValues.asScala.toList)
  }

  implicit class ScalaCondition(val condition: Condition) extends AnyVal {
    def asAws: policy.Condition =
      new policy.Condition()
        .withConditionKey(condition.key)
        .withType(condition.comparisonType)
        .withValues(condition.comparisonValues: _*)
  }

  implicit class ScalaArnConditionComparisonType(val comparisonType: Condition.ArnComparisonType) extends AnyVal {
    def asAws: ArnCondition.ArnComparisonType =
      comparisonType match {
        case Condition.ArnComparisonType.Equals ⇒ ArnCondition.ArnComparisonType.ArnEquals
        case Condition.ArnComparisonType.NotEquals ⇒ ArnCondition.ArnComparisonType.ArnNotEquals
        case Condition.ArnComparisonType.Like ⇒ ArnCondition.ArnComparisonType.ArnLike
        case Condition.ArnComparisonType.NotLike ⇒ ArnCondition.ArnComparisonType.ArnNotLike
      }
  }

  implicit class AwsArnConditionComparisonType(val comparisonType: ArnCondition.ArnComparisonType) extends AnyVal {
    def asScala: Condition.ArnComparisonType =
      comparisonType match {
        case ArnCondition.ArnComparisonType.ArnEquals ⇒ Condition.ArnComparisonType.Equals
        case ArnCondition.ArnComparisonType.ArnNotEquals ⇒ Condition.ArnComparisonType.NotEquals
        case ArnCondition.ArnComparisonType.ArnLike ⇒ Condition.ArnComparisonType.Like
        case ArnCondition.ArnComparisonType.ArnNotLike ⇒ Condition.ArnComparisonType.NotLike
      }
  }

  implicit class ScalaDateConditionComparisonType(val comparisonType: Condition.DateComparisonType) extends AnyVal {
    def asAws: DateCondition.DateComparisonType =
      comparisonType match {
        case Condition.DateComparisonType.Equals ⇒ DateCondition.DateComparisonType.DateEquals
        case Condition.DateComparisonType.NotEquals ⇒ DateCondition.DateComparisonType.DateNotEquals
        case Condition.DateComparisonType.Before ⇒ DateCondition.DateComparisonType.DateLessThan
        case Condition.DateComparisonType.AtOrBefore ⇒ DateCondition.DateComparisonType.DateLessThanEquals
        case Condition.DateComparisonType.After ⇒ DateCondition.DateComparisonType.DateGreaterThan
        case Condition.DateComparisonType.AtOrAfter ⇒ DateCondition.DateComparisonType.DateGreaterThanEquals
      }
  }

  implicit class AwsDateConditionComparisonType(val comparisonType: DateCondition.DateComparisonType) extends AnyVal {
    def asScala: Condition.DateComparisonType =
      comparisonType match {
        case DateCondition.DateComparisonType.DateEquals ⇒ Condition.DateComparisonType.Equals
        case DateCondition.DateComparisonType.DateNotEquals ⇒ Condition.DateComparisonType.NotEquals
        case DateCondition.DateComparisonType.DateLessThan ⇒ Condition.DateComparisonType.Before
        case DateCondition.DateComparisonType.DateLessThanEquals ⇒ Condition.DateComparisonType.AtOrBefore
        case DateCondition.DateComparisonType.DateGreaterThan ⇒ Condition.DateComparisonType.After
        case DateCondition.DateComparisonType.DateGreaterThanEquals ⇒ Condition.DateComparisonType.AtOrAfter
      }
  }

  implicit class ScalaIpAddressConditionComparisonType(val comparisonType: Condition.IpAddressComparisonType) extends AnyVal {
    def asAws: IpAddressCondition.IpAddressComparisonType =
      comparisonType match {
        case Condition.IpAddressComparisonType.IsIn ⇒ IpAddressCondition.IpAddressComparisonType.IpAddress
        case Condition.IpAddressComparisonType.IsNotIn ⇒ IpAddressCondition.IpAddressComparisonType.NotIpAddress
      }
  }

  implicit class AwsIpAddressConditionComparisonType(val comparisonType: IpAddressCondition.IpAddressComparisonType) extends AnyVal {
    def asScala: Condition.IpAddressComparisonType =
      comparisonType match {
        case IpAddressCondition.IpAddressComparisonType.IpAddress⇒ Condition.IpAddressComparisonType.IsIn
        case IpAddressCondition.IpAddressComparisonType.NotIpAddress ⇒ Condition.IpAddressComparisonType.IsNotIn
      }
  }

  implicit class ScalaNumericConditionComparisonType(val comparisonType: Condition.NumericComparisonType) extends AnyVal {
    def asAws: NumericCondition.NumericComparisonType =
      comparisonType match {
        case Condition.NumericComparisonType.Equals ⇒ NumericCondition.NumericComparisonType.NumericEquals
        case Condition.NumericComparisonType.GreaterThan ⇒ NumericCondition.NumericComparisonType.NumericGreaterThan
        case Condition.NumericComparisonType.GreaterThanEquals ⇒ NumericCondition.NumericComparisonType.NumericGreaterThanEquals
        case Condition.NumericComparisonType.LessThan ⇒ NumericCondition.NumericComparisonType.NumericLessThan
        case Condition.NumericComparisonType.LessThanEquals ⇒ NumericCondition.NumericComparisonType.NumericLessThanEquals
        case Condition.NumericComparisonType.NotEquals ⇒ NumericCondition.NumericComparisonType.NumericNotEquals
      }
  }

  implicit class AwsNumericConditionComparisonType(val comparisonType: NumericCondition.NumericComparisonType) extends AnyVal {
    def asScala: Condition.NumericComparisonType =
      comparisonType match {
        case NumericCondition.NumericComparisonType.NumericEquals ⇒ Condition.NumericComparisonType.Equals
        case NumericCondition.NumericComparisonType.NumericGreaterThan ⇒ Condition.NumericComparisonType.GreaterThan
        case NumericCondition.NumericComparisonType.NumericGreaterThanEquals ⇒ Condition.NumericComparisonType.GreaterThanEquals
        case NumericCondition.NumericComparisonType.NumericLessThan ⇒ Condition.NumericComparisonType.LessThan
        case NumericCondition.NumericComparisonType.NumericLessThanEquals ⇒ Condition.NumericComparisonType.LessThanEquals
        case NumericCondition.NumericComparisonType.NumericNotEquals ⇒ Condition.NumericComparisonType.NotEquals
      }
  }

  implicit class ScalaStringConditionComparisonType(val comparisonType: Condition.StringComparisonType) extends AnyVal {
    def asAws: StringCondition.StringComparisonType =
      comparisonType match {
        case Condition.StringComparisonType.Equals ⇒ StringCondition.StringComparisonType.StringEquals
        case Condition.StringComparisonType.NotEquals ⇒ StringCondition.StringComparisonType.StringNotEquals
        case Condition.StringComparisonType.EqualsIgnoreCase ⇒ StringCondition.StringComparisonType.StringEqualsIgnoreCase
        case Condition.StringComparisonType.NotEqualsIgnoreCase ⇒ StringCondition.StringComparisonType.StringNotEqualsIgnoreCase
        case Condition.StringComparisonType.Like ⇒ StringCondition.StringComparisonType.StringLike
        case Condition.StringComparisonType.NotLike ⇒ StringCondition.StringComparisonType.StringNotLike
      }
  }

  implicit class AwsStringConditionComparisonType(val comparisonType: StringCondition.StringComparisonType) extends AnyVal {
    def asScala: Condition.StringComparisonType =
      comparisonType match {
        case StringCondition.StringComparisonType.StringEquals ⇒ Condition.StringComparisonType.Equals
        case StringCondition.StringComparisonType.StringNotEquals ⇒ Condition.StringComparisonType.NotEquals
        case StringCondition.StringComparisonType.StringEqualsIgnoreCase ⇒ Condition.StringComparisonType.EqualsIgnoreCase
        case StringCondition.StringComparisonType.StringNotEqualsIgnoreCase ⇒ Condition.StringComparisonType.NotEqualsIgnoreCase
        case StringCondition.StringComparisonType.StringLike ⇒ Condition.StringComparisonType.Like
        case StringCondition.StringComparisonType.StringNotLike ⇒ Condition.StringComparisonType.NotLike
      }
  }

  implicit class AwsResource(val resource: policy.Resource) extends AnyVal {
    def asScala: Resource =
      resource.getId match {
        case "*" ⇒ Resource.AllResources
        case _   ⇒ Resource(resource.getId)
      }
  }

  implicit class ScalaResource(val resource: Resource) extends AnyVal {
    def asAws: policy.Resource = new policy.Resource(resource.id)
  }

  implicit class AwsStatementEffect(val effect: policy.Statement.Effect) extends AnyVal {
    def asScala: Statement.Effect =
      effect match {
        case policy.Statement.Effect.Allow ⇒ Statement.Effect.Allow
        case policy.Statement.Effect.Deny ⇒ Statement.Effect.Deny
      }
  }

  implicit class ScalaStatementEffect(val effect: Statement.Effect) extends AnyVal {
    def asAws: policy.Statement.Effect =
      effect match {
        case Statement.Effect.Allow ⇒ policy.Statement.Effect.Allow
        case Statement.Effect.Deny ⇒ policy.Statement.Effect.Deny
      }
  }

  implicit class AwsStatement(val statement: policy.Statement) extends AnyVal {
    def asScala: Statement =
      Statement(
        Option(statement.getId),
        asSet(statement.getPrincipals).map(_.asScala),
        statement.getEffect.asScala,
        asList(statement.getActions).map(_.asScala),
        asList(statement.getResources).map(_.asScala),
        asSet(statement.getConditions).map(_.asScala))
  }

  implicit class ScalaStatement(val statement: Statement) extends AnyVal {
    def asAws: policy.Statement = {
      val awsStatement = new policy.Statement(statement.effect.asAws)
      statement.id.foreach(id ⇒ awsStatement.setId(id))
      awsStatement.setPrincipals(statement.principals.map(_.asAws).asJavaCollection)
      awsStatement.setActions(statement.actions.map(_.asAws).asJavaCollection)
      awsStatement.setResources(statement.resources.map(_.asAws).asJavaCollection)
      awsStatement.setConditions(statement.conditions.map(_.asAws).toList.asJava)
      awsStatement
    }
  }

  implicit class AwsPolicy(val awsPolicy: policy.Policy) extends AnyVal {
    def asScala: Policy =
      Policy(
        Option(awsPolicy.getVersion).map(_.asScalaPolicyVersion),
        Option(awsPolicy.getId),
        asList(awsPolicy.getStatements).map(_.asScala))
  }

  implicit class ScalaPolicy(val scalaPolicy: Policy) extends AnyVal {
    def asAws: policy.Policy = {
      val awsPolicy = new policy.Policy()
      scalaPolicy.id.foreach(id ⇒ awsPolicy.setId(id))
      awsPolicy.setStatements(scalaPolicy.statements.map(_.asAws).asJavaCollection)
      awsPolicy
    }
  }

  implicit class AwsRegion(val region: regions.Regions) extends AnyVal {
    def asScala: Region =
      Region.unapply(region.getName)
        .getOrElse(throw new IllegalArgumentException(s"Could not find Scala equivalent for $region"))
  }

  implicit class ScalaRegion(val region: Region) extends AnyVal {
    def asAws: regions.Regions = regions.Regions.fromName(region.name)
  }

  implicit class AwsPrincipalService(val service: policy.Principal.Services) extends AnyVal {
    def asScala: Principal.Service with Principal.Service.AwsEnumerated =
      service match {
        case policy.Principal.Services.AllServices             ⇒ Principal.Service.AllServices
        case policy.Principal.Services.AmazonEC2               ⇒ Principal.Service.AmazonEC2
        case policy.Principal.Services.AmazonElasticTranscoder ⇒ Principal.Service.AmazonElasticTranscoder
        case policy.Principal.Services.AWSCloudHSM             ⇒ Principal.Service.AWSCloudHSM
        case policy.Principal.Services.AWSDataPipeline         ⇒ Principal.Service.AWSDataPipeline
        case policy.Principal.Services.AWSOpsWorks             ⇒ Principal.Service.AWSOpsWorks
      }
  }

  implicit class ScalaPrincipalServiceWithAwsEnum(val service: Principal.Service with Principal.Service.AwsEnumerated) extends AnyVal {
    def asAws: policy.Principal.Services =
      service match {
        case Principal.Service.AllServices             ⇒ policy.Principal.Services.AllServices
        case Principal.Service.AmazonEC2               ⇒ policy.Principal.Services.AmazonEC2
        case Principal.Service.AmazonElasticTranscoder ⇒ policy.Principal.Services.AmazonElasticTranscoder
        case Principal.Service.AWSCloudHSM             ⇒ policy.Principal.Services.AWSCloudHSM
        case Principal.Service.AWSDataPipeline         ⇒ policy.Principal.Services.AWSDataPipeline
        case Principal.Service.AWSOpsWorks             ⇒ policy.Principal.Services.AWSOpsWorks
      }
  }

  implicit class AwsPrincipalWebIdentityProvider(val service: policy.Principal.WebIdentityProviders) extends AnyVal {
    def asScala: Principal.WebIdentityProvider =
      service match {
        case policy.Principal.WebIdentityProviders.AllProviders ⇒ Principal.WebIdentityProvider.AllProviders
        case policy.Principal.WebIdentityProviders.Amazon       ⇒ Principal.WebIdentityProvider.Amazon
        case policy.Principal.WebIdentityProviders.Facebook     ⇒ Principal.WebIdentityProvider.Facebook
        case policy.Principal.WebIdentityProviders.Google       ⇒ Principal.WebIdentityProvider.Google
      }
  }

  implicit class ScalaPrincipalWebIdentityProvider(val service: Principal.WebIdentityProvider) extends AnyVal {
    def asAws: policy.Principal.WebIdentityProviders =
      service match {
        case Principal.WebIdentityProvider.AllProviders ⇒ policy.Principal.WebIdentityProviders.AllProviders
        case Principal.WebIdentityProvider.Amazon       ⇒ policy.Principal.WebIdentityProviders.Amazon
        case Principal.WebIdentityProvider.Facebook     ⇒ policy.Principal.WebIdentityProviders.Facebook
        case Principal.WebIdentityProvider.Google       ⇒ policy.Principal.WebIdentityProviders.Google
      }
  }

  implicit class AwsPolicyVersion(val version: String) extends AnyVal {
    def asScalaPolicyVersion: Policy.Version = Policy.Version.fromId(version)
  }

  implicit class ScalaPolicyVersion(val version: Policy.Version) extends AnyVal {
    def asAws: String = version.id
  }

  private def asList[T](collection: util.Collection[T]): List[T] =
    Option(collection).map(_.asScala.toList).getOrElse(List.empty)

  private def asSet[T](collection: util.Collection[T]): Set[T] =
    Option(collection).map(_.asScala.toSet).getOrElse(Set.empty)
}
