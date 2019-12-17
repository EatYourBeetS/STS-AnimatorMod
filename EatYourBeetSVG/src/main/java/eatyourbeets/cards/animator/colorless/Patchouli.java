package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore_Fire;
import eatyourbeets.cards.animator.special.OrbCore_Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.DynamicCardBuilder;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
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

        Initialize(0,0, 1,1);

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

        if(EffectHistory.TryActivateLimited(this.cardID)){
            DynamicCardBuilder cardBuilder1 = new DynamicCardBuilder(cardID + "_Sun")
                    .SetProperties(-2, CardType.SKILL, color, rarity, target)
                    .SetNumbers(0, 0, 0, 0)
                    .SetText(cardStrings.EXTENDED_DESCRIPTION[0], cardStrings.EXTENDED_DESCRIPTION[1], cardStrings.EXTENDED_DESCRIPTION[1])
                    .SetOnUse((card, player, enemy) ->
                    {
                        GameActions.Bottom.MakeCardInDiscardPile(new OrbCore_Fire());
                        for (AbstractMonster monsters : GameUtilities.GetCurrentEnemies(true))
                        {
                            GameActions.Bottom.ApplyBurning(p, monsters, 5);
                        }
                    });

            DynamicCardBuilder cardBuilder2 = new DynamicCardBuilder(cardID + "_Moon")
                    .SetProperties(-2, CardType.SKILL, color, rarity, target)
                    .SetNumbers(0, 0, 0, 0)
                    .SetText(cardStrings.EXTENDED_DESCRIPTION[2], cardStrings.EXTENDED_DESCRIPTION[3], cardStrings.EXTENDED_DESCRIPTION[3])
                    .SetOnUse((card, player, enemy) ->
                    {
                        GameActions.Bottom.MakeCardInDiscardPile(new OrbCore_Frost());
                        GameActions.Bottom.GainMetallicize(2);
                    });

            CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            choices.group.add(cardBuilder1.Build());
            choices.group.add(cardBuilder2.Build());

            GameActions.Bottom.SelectFromPile(name, 1, choices)
                    .SetOptions(false, false)
                    .AddCallback(m, (enemy, cards) ->
                    {
                        if (cards.size() > 0)
                        {
                            cards.get(0).use(AbstractDungeon.player, (AbstractMonster) enemy);
                        }
                    });
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
}