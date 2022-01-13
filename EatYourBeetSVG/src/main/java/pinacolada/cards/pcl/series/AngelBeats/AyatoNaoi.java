package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.interfaces.subscribers.OnAfterlifeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.stances.WisdomStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AyatoNaoi extends PCLCard implements OnAfterlifeSubscriber
{
    public static final PCLCardData DATA = Register(AyatoNaoi.class).SetSkill(3, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage();
    private static final CardEffectChoice choices = new CardEffectChoice();

    public AyatoNaoi()
    {
        super(DATA);

        Initialize(0, 2, 2, 0);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1, 0, 3);
        SetAffinity_Dark(1, 0, 0);
        SetExhaust(true);
        SetAfterlife(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onAfterlife.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        for (int i = 0; i < magicNumber; i++)
        {
            PCLActions.Bottom.ChannelOrb(new Dark()).AddCallback(o -> {
                if (o.size() > 0 && WisdomStance.IsActive()) {
                    PCLActions.Bottom.TriggerOrbPassive(o.get(0), 1);
                }
            });
        }

        if (!WisdomStance.IsActive()) {
            PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
        }
    }

    @Override
    public void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard) {
        if (playedCard == this) {
            PCLActions.Bottom.Callback(() ->
            {
                int totalDamage = 0;
                for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true))
                {
                    totalDamage += PCLGameUtilities.GetPCLIntent(mo).GetDamage(true);
                }

                if (totalDamage > 0)
                {
                    PCLActions.Top.VFX(new OfferingEffect(), Settings.FAST_MODE ? 0.1F : 0.5F);
                    PCLActions.Bottom.ApplyRippled(TargetHelper.Enemies(), totalDamage);
                }
            });
        }
    }
}