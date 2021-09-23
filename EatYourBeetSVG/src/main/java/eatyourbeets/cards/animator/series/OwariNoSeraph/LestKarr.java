package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Burn;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class LestKarr extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(LestKarr.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage().PostInitialize((data) -> {
        data.AddPreview(AffinityToken.GetCard(Affinity.Dark), true);
        data.AddPreview(new Status_Burn(), false);
    });

    public LestKarr() {
        super(DATA);
        this.Initialize(0, 2, 2, 3);
        this.SetAffinity_Dark(1, 1, 1);
        this.SetAffinity_Blue(1);
    }

    public boolean ShouldCancel() {
        return player.masterDeck.size() <= 16;
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (CombatStats.TryActivateSemiLimited(this.cardID)) {
            GameActions.Bottom.GainIntellect(1, true);
            GameActions.Bottom.GainCorruption(1, true);
            GameActions.Bottom.SFX("ATTACK_FIRE", 0.75F, 0.75F);
            GameActions.Bottom.MakeCardInDrawPile(new Status_Burn());
            GameActions.Bottom.MakeCardInDiscardPile(new Status_Burn());
            GameActions.Bottom.Flash(this);
        }

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(this.block);
        if (IntellectStance.IsActive()) {
            GameActions.Bottom.SFX("ATTACK_MAGIC_FAST_1", 0.75F, 0.8F).SetDuration(0.2F, true);
            GameActions.Bottom.ObtainAffinityToken(Affinity.Dark, this.upgraded);
        } else {
            GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
        }

    }
}