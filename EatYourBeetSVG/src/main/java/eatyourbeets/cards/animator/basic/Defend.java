package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Defend extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Defend.class).SetSkill(1, CardRarity.BASIC, EYBCardTarget.None);

    public Defend(String id, int cost, CardTarget target)
    {
        super(GetStaticData(id), id, AnimatorResources.GetCardImage(DATA.ID + "Alt"), cost, CardType.SKILL, CardColor.COLORLESS,
                CardRarity.BASIC, target);

        //setBannerTexture("images\\cardui\\512\\banner_uncommon.png","images\\cardui\\1024\\banner_uncommon.png");

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_DEFEND);
        this.tags.add(GR.Enums.CardTags.IMPROVED_DEFEND);
    }

    public Defend()
    {
        super(DATA);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
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