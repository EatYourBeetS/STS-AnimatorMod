package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class MonaMegistus extends PCLCard implements OnEvokeOrbSubscriber {
    public static final PCLCardData DATA = Register(MonaMegistus.class)
            .SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.Normal)
            .SetSeriesFromClassPackage(true);

    public MonaMegistus() {
        super(DATA);

        Initialize(0, 2, 2, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(1, 0, 3);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(PCLAffinity.Blue, 1);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onEvokeOrb.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.Scry(magicNumber)
                .AddCallback(cards -> {
                    int commons = PCLJUtils.Count(cards, card -> card.rarity == CardRarity.COMMON || card.rarity == CardRarity.BASIC);
                    if (commons > 0) {
                        PCLActions.Bottom.ApplyRippled(TargetHelper.Normal(m), secondaryValue * commons);
                    }
                });
    }

    @Override
    public void OnEvokeOrb(AbstractOrb abstractOrb) {
        if (player.hand.contains(this) && CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.GainSorcery(magicNumber);
        }
    }
}