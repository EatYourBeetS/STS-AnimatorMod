package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.relics.pcl.ExquisiteBloodVial;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class KrulTepes extends PCLCard
{
    public static final PCLCardData DATA = Register(KrulTepes.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    private static final AbstractRelic relicReward = new BloodVial();
    protected boolean gainTempHP = false;

    public KrulTepes()
    {
        super(DATA);

        Initialize(13, 0, 2, 4);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Dark(2, 0, 2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            PCLGameUtilities.GetPCLIntent(m).AddWeak();
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return gainTempHP ? TempHPAttribute.Instance.SetCard(this, false).SetText(secondaryValue, Colors.Cream(1f)) : null;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.gainTempHP = (enemy != null && enemy.hasPower(WeakPower.POWER_ID));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m != null)
        {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE)
                    .forEach(d -> d.SetDamageEffect(e -> PCLGameEffects.List.Add(VFX.Bite(e.hb, Color.SCARLET)).duration)
            .AddCallback(enemy ->
            {
                if (PCLGameUtilities.InEliteOrBossRoom() && PCLGameUtilities.IsFatal(enemy, false) && info.TryActivateLimited())
                {
                    ObtainReward();
                }
            }));

            PCLActions.Bottom.ApplyWeak(p, m, secondaryValue);

            if (gainTempHP)
            {
                PCLActions.Bottom.GainTemporaryHP(secondaryValue);
            }
        }
    }

    public void ObtainReward()
    {
        int totalRelics = 0;
        final ArrayList<AbstractRelic> relics = player.relics;
        for (AbstractRelic relic : relics)
        {
            if (relic.relicId.equals(relicReward.relicId))
            {
                totalRelics += 1;
            }
            else if (relic.relicId.equals(ExquisiteBloodVial.ID))
            {
                totalRelics = -1;
                break;
            }
        }

        if (UnnamedReignRelic.IsEquipped())
        {
            PCLGameUtilities.GetCurrentRoom(true).addRelicToRewards(new AncientMedallion());
        }
        else
        {
            PCLGameUtilities.GetCurrentRoom(true).addRelicToRewards(relicReward.makeCopy());
        }
    }
}