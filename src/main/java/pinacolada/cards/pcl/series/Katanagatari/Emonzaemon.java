package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Emonzaemon_EntouJyuu;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;
public class Emonzaemon extends PCLCard
{
    public static final PCLCardData DATA = Register(Emonzaemon.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Emonzaemon_EntouJyuu(), true));

    public Emonzaemon()
    {
        super(DATA);

        Initialize(4, 0);
        SetUpgrade(2, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1);
        SetHitCount(2);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.55f, 0.65f));

        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.MakeCardInDrawPile(new Emonzaemon_EntouJyuu())
            .SetDestination(CardSelection.Random)
            .SetUpgrade(upgraded, false);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.AddAffinity(PCLAffinity.Green, 1);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        if (CombatStats.CanActivateLimited(cardID))
        {
            final ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
            final int size = cardsPlayed.size();
            final int count = tryUse ? 3 : 2;
            if (size >= count)
            {
                boolean canActivate = true;
                for (int i = 1; i <= count; i++)
                {
                    if (cardsPlayed.get(size - i).type != CardType.ATTACK) {
                        canActivate = false;
                        break;
                    }
                }

                if (canActivate)
                {
                    return !tryUse || CombatStats.TryActivateLimited(cardID);
                }
            }
        }

        return false;
    }
}