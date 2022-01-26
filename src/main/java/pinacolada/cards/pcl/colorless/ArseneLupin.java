package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.special.StolenGoldPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ArseneLupin extends PCLCard
{
    public static final PCLCardData DATA = Register(ArseneLupin.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Ranged, PCLCardTarget.Random).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Lupin).SetMaxCopies(2);

    public ArseneLupin()
    {
        super(DATA);

        Initialize(4, 0, 2 , 50);
        SetUpgrade(0, 0, 1 , -10);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Dark(1);
        SetAffinity_Green(0,0,1);

        SetExhaust(true);
        SetHealing(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + Math.floorDiv(player.gold, secondaryValue));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.GUNSHOT);
        PCLActions.Last.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                for (int i = 0; i < cards.size(); i ++) {
                    final AbstractMonster enemy = PCLGameUtilities.GetRandomEnemy(true);
                    if (enemy != null)
                    {
                        PCLActions.Bottom.StackPower(new StolenGoldPower(enemy, magicNumber));
                    }
                }
            }
        });
    }
}