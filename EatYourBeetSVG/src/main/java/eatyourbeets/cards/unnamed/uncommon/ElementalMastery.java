package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;

public class ElementalMastery extends UnnamedCard
{
    public static final EYBCardData DATA = Register(ElementalMastery.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public ElementalMastery()
    {
        super(DATA);

        Initialize(0, 5, 2, 5);
        SetUpgrade(0, 2, 0, 2);

        SetCardPreview(this::CanMotivate);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_SLOW_2, 0.6f, 0.6f);
        GameActions.Bottom.SelectFromPile(name, magicNumber, p.drawPile)
        .SetOptions(true, true, true)
        .SetFilter(this::CanMotivate)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                for (int i = 0; i < cards.size(); i++)
                {
                    GameActions.Bottom.Motivate(cards.get(i), 1)
                    .SetGroup(player.drawPile)
                    .ShowEffect(true, i);
                }
            }
        });
        GameActions.Bottom.ModifyAllInstances(uuid)
        .AddCallback(c ->
        {
            CostModifiers.For(c).Add(1);
            BlockModifiers.For(c).Add(secondaryValue);
        });
    }

    private boolean CanMotivate(AbstractCard card)
    {
        return card instanceof EYBCard && ((EYBCard)card).attackType == EYBAttackType.Elemental && card.costForTurn > 0;
    }
}