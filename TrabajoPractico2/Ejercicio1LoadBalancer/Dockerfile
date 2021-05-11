FROM nginx:stable-alpine
RUN wget -q https://releases.hashicorp.com/consul-template/0.25.2/consul-template_0.25.2_linux_amd64.zip -O consul-template.zip && unzip consul-template.zip && mv consul-template /bin/consul-template && rm consul-template.zip
RUN mkdir -p /etc/consul-templates/
COPY ./load-balancer.conf.ctmpl /etc/nginx/conf.d/load-balancer.conf.ctmpl
COPY ./consul-template-config.hcl /etc/consul-templates/consul-template-config.hcl
RUN consul-template -config=/etc/consul-templates/consul-template-config.hcl
RUN cat /etc/nginx/conf.d/load-balancer.conf
