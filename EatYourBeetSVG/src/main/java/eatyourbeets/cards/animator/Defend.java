package eatyourbeets.cards.animator;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.Synergy;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import patches.AbstractEnums;

public class Defend extends AnimatorCard
{
    public static final String ID = Register(Defend.class.getSimpleName());

    public Defend(String id, int cost, CardTarget target)
    {
        super(staticCardData.get(id), id, Resources_Animator.GetCardImage(ID + "Alt"), cost, CardType.SKILL, CardColor.COLORLESS,
                CardRarity.BASIC, target);

        //setBannerTexture("images\\cardui\\512\\banner_uncommon.png","images\\cardui\\1024\\banner_uncommon.png");

        this.tags.add(BaseModCardTags.BASIC_DEFEND);
        this.tags.add(AbstractEnums.CardTags.IMPROVED_DEFEND);
    }

    public Defend() 
    {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);

        Initialize(0, 5);

        this.tags.add(BaseModCardTags.BASIC_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(this.block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }

    @Override
    public void SetSynergy(Synergy synergy)
    {
        if (GameUtilities.GetActualAscensionLevel() >= 9)
        {
            super.SetSynergy(synergy);
        }
    }
}