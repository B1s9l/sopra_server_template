package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-26T23:10:26+0100",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 17.0.10 (Eclipse Adoptium)"
)
public class DTOMapperImpl implements DTOMapper {

    @Override
    public User convertUserPostDTOtoEntity(UserPostDTO userPostDTO) {
        if ( userPostDTO == null ) {
            return null;
        }

        User user = new User();

        user.setBirthday( userPostDTO.getBirthday() );
        user.setPassword( userPostDTO.getPassword() );
        user.setUserId( userPostDTO.getUserId() );
        user.setUsername( userPostDTO.getUsername() );
        user.setCreationDate( userPostDTO.getCreationDate() );

        return user;
    }

    @Override
    public UserGetDTO convertEntityToUserGetDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserGetDTO userGetDTO = new UserGetDTO();

        userGetDTO.setBirthday( user.getBirthday() );
        userGetDTO.setPassword( user.getPassword() );
        userGetDTO.setCreationDate( user.getCreationDate() );
        userGetDTO.setUserId( user.getUserId() );
        userGetDTO.setUsername( user.getUsername() );
        userGetDTO.setStatus( user.getStatus() );
        userGetDTO.setToken( user.getToken() );

        return userGetDTO;
    }
}
