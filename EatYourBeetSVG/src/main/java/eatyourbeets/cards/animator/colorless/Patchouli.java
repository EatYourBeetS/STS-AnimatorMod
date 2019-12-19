package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.animator.special.OrbCore_Fire;
import eatyourbeets.cards.animator.special.OrbCore_Frost;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Patchouli extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Patchouli.class.getSimpleName(), EYBCardBadge.Special);

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Patchouli()
    {
        super(ID, 2, AbstractCard.CardType.SKILL, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

        Initialize(0, 0, 1, 1);

        SetEvokeOrbCount(magicNumber);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        Spellcaster.ApplyScaling(this, 5);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.ChannelRandomOrb(true);
        }

        GameActions.Bottom.GainIntellect(secondaryValue);

        if (EffectHistory.TryActivateLimited(this.cardID))
        {
            CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            choices.group.add(CreateSun().Build());
            choices.group.add(CreateMoon().Build());

            GameActions.Bottom.SelectFromPile(name, 1, choices)
            .SetMessage(CardRewardScreen.TEXT[1])
            .SetOptions(false, false)
            .AddCallback(m, (enemy, cards) -> cards.get(0).use(AbstractDungeon.player, (AbstractMonster) enemy));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
            SetEvokeOrbCount(magicNumber);
        }
    }

    private DynamicCardBuilder CreateSun()
    {
        return new DynamicCardBuilder(cardID + "_Sun")
        .SetProperties(-2, CardType.SKILL, color, rarity, target)
        .SetNumbers(0, 0, 5)
        .SetText(cardStrings.EXTENDED_DESCRIPTION[0], cardStrings.EXTENDED_DESCRIPTION[1], null)
        .SetOnUse((card, player, enemy) ->
        {
            GameActions.Bottom.MakeCardInDiscardPile(new OrbCore_Fire());
            for (AbstractMonster monster : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.ApplyBurning(player, monster, card.magicNumber);
            }
        });
    }

    private DynamicCardBuilder CreateMoon()
    {
        return new DynamicCardBuilder(cardID + "_Moon")
        .SetProperties(-2, CardType.SKILL, color, rarity, target)
        .SetNumbers(0, 0, 2)
        .SetText(cardStrings.EXTENDED_DESCRIPTION[2], cardStrings.EXTENDED_DESCRIPTION[3], null)
        .SetOnUse((card, player, enemy) ->
        {
            GameActions.Bottom.MakeCardInDiscardPile(new OrbCore_Frost());
            GameActions.Bottom.GainMetallicize(card.magicNumber);
        });
    }
}