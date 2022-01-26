package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.replacement.TemporaryDrawReductionPower;
import pinacolada.utilities.PCLActions;

import java.util.concurrent.atomic.AtomicBoolean;

public class SuikaIbuki extends PCLCard
{
    public static final PCLCardData DATA = Register(SuikaIbuki.class).SetAttack(2, CardRarity.COMMON, PCLAttackType.Normal, PCLCardTarget.AoE).SetSeriesFromClassPackage();

    public SuikaIbuki()
    {
        super(DATA);

        Initialize(8, 8, 2, 2);
        SetUpgrade(1, 2, 1, 0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Blue(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AtomicBoolean shouldDrawLess = new AtomicBoolean(true);
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.GainBlock(block);

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (int i = 0; i < Math.min(magicNumber, player.drawPile.size()); ++i)
        {
            group.addToTop(player.drawPile.group.get(player.drawPile.size() - i - 1));
        }

        PCLActions.Bottom.ExhaustFromPile(name, 1, group).SetOptions(false,true).AddCallback(cards -> {
            if (cards.size() > 0) {
                shouldDrawLess.set(false);
            }
            for (AbstractCard card : cards) {
                if (card.type == CardType.ATTACK) {
                    PCLActions.Bottom.AddAffinity(PCLAffinity.Red, secondaryValue);
                }
            }
        });

        PCLActions.Last.Callback(() -> {
           if (shouldDrawLess.get()) {
               PCLActions.Bottom.StackPower(new TemporaryDrawReductionPower(player, 1));
           }
        });
    }
}

