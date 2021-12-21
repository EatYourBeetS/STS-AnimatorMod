package pinacolada.cards.pcl.series.DateALive;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardPreview;
import pinacolada.cards.pcl.special.Zadkiel;
import pinacolada.utilities.PCLActions;

public class YoshinoHimekawa extends PCLCard {
    public static final PCLCardData DATA = Register(YoshinoHimekawa.class).SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new YoshinoHimekawa(true), false);
                data.AddPreview(new Zadkiel(), false);
            });
    private boolean transformed = false;

    private YoshinoHimekawa(boolean transformed) {
        this();

        SetTransformed(transformed);
    }

    public YoshinoHimekawa() {
        super(DATA);

        Initialize(0, 2, 5, 2);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Star(0,0,1);

        SetEthereal(true);
        SetExhaust(true);
        SetHaste(true);
        SetCostUpgrade(-1);
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb) {
        if (!transformed) {
            super.renderUpgradePreview(sb);
        }
    }

    @Override
    public PCLCardPreview GetCardPreview() {
        if (transformed) {
            return null;
        }

        return super.GetCardPreview();
    }

    @Override
    public void triggerWhenDrawn() {
        if (!transformed) {
            PCLActions.Top.Discard(this, player.hand).ShowEffect(true, true)
                    .AddCallback(() -> {
                        SetTransformed(true);
                        PCLActions.Bottom.GainBlur(1);
                    })
                    .SetDuration(0.15f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        if (transformed) {
            PCLActions.Bottom.GainBlur(secondaryValue);
            PCLActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), magicNumber).ShowEffect(true, true);
        }
        else {
            PCLActions.Bottom.GainBlur(secondaryValue);
            if (CombatStats.TryActivateLimited(cardID)) {
                PCLActions.Top.MakeCardInDiscardPile(new Zadkiel()).SetUpgrade(upgraded, false);
            }
        }
    }

    private void SetTransformed(boolean value) {
        transformed = value;

        if (transformed) {
            LoadImage("2");
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        } else {
            LoadImage(null);
            cardText.OverrideDescription(null, true);
        }
    }
}