package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.common.ElectrifiedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

import java.util.HashMap;
import java.util.UUID;

public class KujouSara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KujouSara.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self).SetSeriesFromClassPackage(true);
    private static HashMap<UUID, Integer> buffs;

    public KujouSara()
    {
        super(DATA);

        Initialize(0, 9, 3, 20);
        SetUpgrade(0, 0, 1);
        SetAffinity_Green(1,0,2);
        SetAffinity_Orange(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(Affinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        buffs = CombatStats.GetCombatData(cardID, null);
        if (buffs == null)
        {
            buffs = new HashMap<>();
            CombatStats.SetCombatData(cardID, buffs);
        }

        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.IncreaseScaling(player.hand, player.hand.size(), Affinity.Green, 1).SetFilter(c -> c.uuid != uuid).SetFilter(c ->  buffs.getOrDefault(c.uuid, 0) < magicNumber).AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                JUtils.IncrementMapElement(buffs, c.uuid, 1);
            }
        });

        GameActions.Last.DiscardFromHand(name, player.hand.size() - 1, true)
                .AddCallback(cards -> {
            if (cards.size() > 0) {
                GameActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), cards.size() * magicNumber);
            }
        });

        if (IsStarter() && info.TryActivateLimited()) {
            GameActions.Bottom.Callback(() -> CommonTriggerablePower.AddEffectBonus(ElectrifiedPower.POWER_ID, secondaryValue));
        }
    }
}