package com.monsanto.arch.awsutil.identitymanagement

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.monsanto.arch.awsutil.StreamingAwsClient
import com.monsanto.arch.awsutil.identitymanagement.model._

trait StreamingIdentityManagementClient extends StreamingAwsClient {
  /** Returns a flow that given a list roles request will emit zero or more matching roles. */
  def roleLister: Flow[ListRolesRequest, Role, NotUsed]

  /** Returns a flow that a create role request will create and emmit the resulting role. */
  def roleCreator: Flow[CreateRoleRequest, Role, NotUsed]

  /** Returns a flow that given a role name will delete it and emit the name. */
  def roleDeleter: Flow[String, String, NotUsed]

  /** Returns a flow that will attach a managed policy to a role and then emit the role name. */
  def rolePolicyAttacher: Flow[AttachRolePolicyRequest, String, NotUsed]

  /** Returns a flow that will detach a managed policy from a role and then emit the role name. */
  def rolePolicyDetacher: Flow[DetachRolePolicyRequest, String, NotUsed]

  /** Returns a flow that, given a request to list attached policies for a role, will emit zero or more attached
    * policies.
    */
  def attachedRolePolicyLister: Flow[ListAttachedRolePoliciesRequest, AttachedPolicy, NotUsed]

  /** Returns a flow that will emit a user’s information given a request. */
  def userGetter: Flow[GetUserRequest, User, NotUsed]

  /** Returns a flow that will process a request to create a policy and emit the resulting policy. */
  def policyCreator: Flow[CreatePolicyRequest, ManagedPolicy, NotUsed]

  /** Returns a flow that given a managed policy ARN will delete it and emit the ARN of the deleted policy. */
  def policyDeleter: Flow[PolicyArn, PolicyArn, NotUsed]

  /** Returns a flow that given a policy ARN it will emit a corresponding `ManagedPolicy`. */
  def policyGetter: Flow[PolicyArn, ManagedPolicy, NotUsed]

  /** Returns a flow that given a request will emit all matching policies. */
  def policyLister: Flow[ListPoliciesRequest, ManagedPolicy, NotUsed]

  /** Returns a flow that will process a request to create a new managed policy version and emit the resulting managed
    * policy version.
    */
  def policyVersionCreator: Flow[CreatePolicyVersionRequest, ManagedPolicyVersion, NotUsed]

  /** Returns a flow that will process a request to delete a managed policy
    * version and will emit the ARN of ARN of policy from which a version was
    * deleted.
    */
  def policyVersionDeleter: Flow[DeletePolicyVersionRequest, PolicyArn, NotUsed]

  /** Returns a flow that given a request to retrieve a managed policy version
    * will emit the corresponding object.
    */
  def policyVersionGetter: Flow[GetPolicyVersionRequest, ManagedPolicyVersion, NotUsed]

  /** Returns a flow that given a managed policy ARN will emit all of its versions. */
  def policyVersionLister: Flow[PolicyArn, ManagedPolicyVersion, NotUsed]

  /** Returns a flow that processes request to set a default policy version and emits
    * the ARNs of the modified policies.
    */
  def defaultPolicyVersionSetter: Flow[SetDefaultPolicyVersionRequest, PolicyArn, NotUsed]
}
