package eatyourbeets.cards.animatorClassic.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class ImprovedDefend extends ImprovedBasicCard
{
    public static final EYBCardData DATA = Register(ImprovedDefend.class)
            .SetColor(CardColor.COLORLESS).SetSkill(1, CardRarity.BASIC, EYBCardTarget.None)
            .SetImagePath(GR.GetCardImage(eatyourbeets.cards.animator.basic.Defend.DATA.ID + "Alt1"));

    public ImprovedDefend()
    {
        super(DATA, GR.GetCardImage(eatyourbeets.cards.animator.basic.Defend.DATA.ID + "Alt2"));

        Initialize(0, 6);
        SetUpgrade(0, 2);

        SetTag(CardTags.STARTER_DEFEND, true);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return super.HasDirectSynergy(other) || other.rarity == CardRarity.COMMON;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }
}