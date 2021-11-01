package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.TemporaryDrawReductionPower;
import eatyourbeets.utilities.GameActions;

public class SuikaIbuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuikaIbuki.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public SuikaIbuki()
    {
        super(DATA);

        Initialize(8, 9, 2, 2);
        SetUpgrade(1, 2, 1, 0);
        SetAffinity_Red(1, 1, 1);
        SetAffinity_Blue(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.GainBlock(block);

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (int i = 0; i < Math.min(magicNumber, player.drawPile.size()); ++i)
        {
            group.addToTop(player.drawPile.group.get(player.drawPile.size() - i - 1));
        }

        GameActions.Bottom.ExhaustFromPile(name, 1, group).SetOptions(false, true).AddCallback(cards -> {
            if (cards.size() <= 0) {
                GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(player, 1));
            }
            else {
                for (AbstractCard card : cards) {
                    if (card.type == CardType.ATTACK) {
                        CombatStats.Affinities.AddAffinity(Affinity.Red, secondaryValue);
                    }
                }
            }
        });
    }
}

