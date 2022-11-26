# MS-Quokka-Marketplace-System

### Branching Strategy
- main branch (base): 
  - Pull Request (PR) required to push changes into the main branch. 
  - Push to main branch should only be done when we are ready to create a tag for submission purposes
  - At least one person need to review and approve the PR
- development branch: 
  - PR required to push changes into the development branch. 
  - At least one person need to review and approve the PR
  - Note: documentation upload will be done in the development branch directly, 
    as no PR is needed for documentation update.
- feature branch:
  - Created for each feature's development.
  - Naming strategy:
    - "feature/[name of feature]" - for feature development
    - "bugfix/[name of feature>]" - for debugging purposes only

### Link to the deployed application in Heroku
https://ms-quokka.herokuapp.com/

### How to Use Existing Data
There are three different accounts that preloaded in the database, they are 'buyer account', 'seller account' and 'admin account'. The account credentials are provided below:
- Accounts:
  - Buyer Account:
    - Email: buyer@gmail.com
    - Password: MPstbMX7
  - Seller Account:
    - Email: seller@gmail.com
    - Password: W7db9oM7
  - Admin:
    - Email: admin@gmail.com
    - Password: lB5f0dXj

We have also preloaded all the products and listings for the user to interact with. 